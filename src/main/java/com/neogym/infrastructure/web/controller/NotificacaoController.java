
package com.neogym.infrastructure.web.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.neogym.application.dto.request.ChatNotificationRequest;
import com.neogym.application.dto.request.RegistrarDispositivoRequest;
import com.neogym.application.port.out.AccessTokenPort;
import com.neogym.application.usecase.NotificacaoService;
import com.neogym.infrastructure.persistence.entity.UsuarioJpaEntity;
import com.neogym.infrastructure.persistence.repository.UsuarioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {

    private final AccessTokenPort accessTokenPort;
    private final UsuarioJpaRepository usuarioRepository;
    private final NotificacaoService notificacaoService;
    private final FirebaseAuth firebaseAuth;

    @PostMapping("/dispositivo")
    public ResponseEntity<Void> registrarDispositivo(
            @RequestHeader(
                    value = HttpHeaders.AUTHORIZATION,
                    required = false
            ) String authorization,
            @RequestBody RegistrarDispositivoRequest request
    ) {
        UsuarioJpaEntity usuario = obterUsuarioAutenticado(authorization);

        if (request.firebaseIdToken() == null
                || request.firebaseIdToken().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O token de autenticação Firebase é obrigatório."
            );
        }

        final FirebaseToken identidadeFirebase;

        try {
            identidadeFirebase =
                    firebaseAuth.verifyIdToken(request.firebaseIdToken());
        } catch (FirebaseAuthException exception) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Token de autenticação Firebase inválido."
            );
        }

        notificacaoService.registrarDispositivo(
                usuario.getId(),
                request,
                identidadeFirebase.getUid()
        );

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/chat")
    public ResponseEntity<Void> notificarChat(
            @RequestHeader(
                    value = HttpHeaders.AUTHORIZATION,
                    required = false
            ) String authorization,
            @RequestBody ChatNotificationRequest request
    ) throws FirebaseMessagingException {

        UsuarioJpaEntity usuario = obterUsuarioAutenticado(authorization);

        notificacaoService.notificarChat(
                usuario.getId(),
                request
        );

        return ResponseEntity.noContent().build();
    }

    private UsuarioJpaEntity obterUsuarioAutenticado(
            String authorization
    ) {
        if (authorization == null
                || !authorization.startsWith("Bearer ")) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Token de acesso não informado."
            );
        }

        String token = authorization.substring(7).trim();

        if (token.isBlank() || !accessTokenPort.validar(token)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Token de acesso inválido ou expirado."
            );
        }

        String email = accessTokenPort.extrairEmail(token);

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Usuário não encontrado."
                ));
    }
}
