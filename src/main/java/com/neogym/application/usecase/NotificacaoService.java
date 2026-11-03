
package com.neogym.application.usecase;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.neogym.application.dto.request.ChatNotificationRequest;
import com.neogym.application.dto.request.RegistrarDispositivoRequest;
import com.neogym.infrastructure.persistence.entity.NotificacaoDispositivoJpaEntity;
import com.neogym.infrastructure.persistence.repository.NotificacaoDispositivoJpaRepository;
import com.neogym.infrastructure.persistence.repository.UsuarioJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final NotificacaoDispositivoJpaRepository dispositivoRepository;
    private final UsuarioJpaRepository usuarioRepository;
    private final FirebaseMessaging firebaseMessaging;
    private final Firestore firestore;

    private static final Pattern CHAT_PATTERN =
            Pattern.compile("^chat_(\\d+)_(\\d+)$");

    public void registrarDispositivo(
            Long usuarioId,
            RegistrarDispositivoRequest request,
            String firebaseUid
    ) {
        if (request.token() == null
                || request.token().isBlank()
                || request.token().length() > 4096) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Token FCM inválido."
            );
        }

        if (firebaseUid == null || firebaseUid.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Identidade Firebase inválida."
            );
        }

        NotificacaoDispositivoJpaEntity dispositivo =
                dispositivoRepository.findByToken(request.token())
                        .orElseGet(NotificacaoDispositivoJpaEntity::new);

        dispositivo.setUsuarioId(usuarioId);
        dispositivo.setFirebaseUid(firebaseUid);
        dispositivo.setToken(request.token());
        dispositivo.setPlataforma(request.plataforma());
        dispositivo.setAtualizadoEm(LocalDateTime.now());

        dispositivoRepository.save(dispositivo);
    }

    public void notificarChat(
            Long usuarioAutenticadoId,
            ChatNotificationRequest request
    ) throws FirebaseMessagingException {

        if (usuarioAutenticadoId == null
                || request == null
                || request.chatId() == null
                || request.messageId() == null
                || request.messageId().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Dados da notificação incompletos."
            );
        }

        Matcher matcher = CHAT_PATTERN.matcher(request.chatId());

        if (!matcher.matches()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Formato do chat inválido."
            );
        }

        long usuarioA = Long.parseLong(matcher.group(1));
        long usuarioB = Long.parseLong(matcher.group(2));

        if (usuarioA == usuarioB
                || (usuarioAutenticadoId != usuarioA
                && usuarioAutenticadoId != usuarioB)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não pertence a esta conversa."
            );
        }

        long destinatarioId = usuarioAutenticadoId == usuarioA
                ? usuarioB
                : usuarioA;

        if (!usuarioRepository.existsById(destinatarioId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Destinatário não encontrado."
            );
        }

        // Lê a mensagem salva no Firestore.
        // O cliente não pode definir livremente o remetente
        // nem o conteúdo usado para gerar a notificação.
        final DocumentSnapshot snapshot;

        try {
            snapshot = firestore
                    .collection("chats")
                    .document(request.chatId())
                    .collection("messages")
                    .document(request.messageId())
                    .get()
                    .get();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();

            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Consulta da mensagem interrompida."
            );
        } catch (ExecutionException exception) {
            log.error(
                    "Erro ao consultar mensagem no Firestore.",
                    exception
            );

            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Não foi possível consultar a mensagem."
            );
        }

        if (!snapshot.exists()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Mensagem não encontrada."
            );
        }

        Map<String, Object> dados = snapshot.getData();

        if (dados == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Documento de mensagem inválido."
            );
        }

        Long remetenteSalvo = converterLong(
                dados.get("senderUsuarioId")
        );

        if (!usuarioAutenticadoId.equals(remetenteSalvo)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "A mensagem não pertence ao usuário autenticado."
            );
        }

        Long destinatarioSalvo = converterLong(
                dados.get("recipientUsuarioId")
        );

        if (destinatarioSalvo != null
                && destinatarioSalvo != destinatarioId) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "O destinatário salvo não corresponde ao chat."
            );
        }

        List<NotificacaoDispositivoJpaEntity> dispositivos =
                dispositivoRepository.findAllByUsuarioId(destinatarioId);

        List<String> tokens = dispositivos.stream()
                .map(NotificacaoDispositivoJpaEntity::getToken)
                .filter(token -> token != null && !token.isBlank())
                .distinct()
                .toList();

        if (tokens.isEmpty()) {
            log.info(
                    "Nenhum dispositivo registrado para o usuário {}.",
                    destinatarioId
            );
            return;
        }

        String nome = textoOuPadrao(
                dados.get("senderName"),
                "Nova mensagem"
        );

        String texto = textoOuPadrao(
                dados.get("text"),
                dados.get("imageUrl") != null
                        ? "Enviou uma imagem."
                        : "Você recebeu uma nova mensagem."
        );

        if (texto.length() > 180) {
            texto = texto.substring(0, 177) + "...";
        }

        Map<String, String> data = new HashMap<>();
        data.put("type", "chat");
        data.put("chatId", request.chatId());
        data.put("messageId", request.messageId());
        data.put("senderName", nome);
        data.put("senderUsuarioId",
                String.valueOf(usuarioAutenticadoId));
        data.put("recipientUsuarioId",
                String.valueOf(destinatarioId));

        Object imageUrl = dados.get("imageUrl");

        if (imageUrl != null && !imageUrl.toString().isBlank()) {
            data.put("imageUrl", imageUrl.toString());
        }

        // FCM aceita até 500 tokens em cada envio multicast.
        for (int inicio = 0; inicio < tokens.size(); inicio += 500) {
            int fim = Math.min(inicio + 500, tokens.size());

            List<String> lote = new ArrayList<>(
                    tokens.subList(inicio, fim)
            );

            MulticastMessage mensagem = MulticastMessage.builder()
                    .addAllTokens(lote)
                    .setNotification(
                            Notification.builder()
                                    .setTitle(nome)
                                    .setBody(texto)
                                    .build()
                    )
                    .putAllData(data)
                    .build();

            BatchResponse resposta =
                    firebaseMessaging.sendEachForMulticast(mensagem);

            if (resposta.getFailureCount() > 0) {
                log.warn(
                        "Falhas no envio de notificações: {} de {}.",
                        resposta.getFailureCount(),
                        lote.size()
                );
            }
        }
    }

    private Long converterLong(Object valor) {
        if (valor instanceof Number numero) {
            return numero.longValue();
        }

        if (valor instanceof String texto) {
            try {
                return Long.parseLong(texto);
            } catch (NumberFormatException ignored) {
                return null;
            }
        }

        return null;
    }

    private String textoOuPadrao(Object valor, String padrao) {
        if (valor == null || valor.toString().isBlank()) {
            return padrao;
        }

        return valor.toString().trim();
    }
}
