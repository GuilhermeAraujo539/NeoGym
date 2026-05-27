package com.neogym.application.usecase;

import com.neogym.application.dto.response.CredencialResponse;
import com.neogym.application.port.out.CredencialRepositoryPort;
import com.neogym.application.port.out.FileStoragePort;
import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.domain.entity.Credencial;
import com.neogym.domain.entity.Usuario;
import com.neogym.domain.enums.StatusCredencial;
import com.neogym.domain.enums.TipoCredencial;
import com.neogym.domain.exception.ArquivoInvalidoException;
import com.neogym.domain.exception.OperacaoNaoPermitidaException;
import com.neogym.domain.exception.UsuarioNaoEncontradoException;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Caso de uso: envio de documento de credencial pelo profissional.
 * Puro Java — zero imports de Spring/Jakarta além de MultipartFile (necessário para upload).
 */
public class EnviarCredencialService {

    private static final long   MAX_TAMANHO_BYTES = 10 * 1024 * 1024; // 10MB
    private static final List<String> TIPOS_PERMITIDOS =
            List.of("image/jpeg", "image/png", "application/pdf");

    private final CredencialRepositoryPort credencialRepository;
    private final UsuarioRepositoryPort    usuarioRepository;
    private final FileStoragePort          fileStorage;

    public EnviarCredencialService(
            CredencialRepositoryPort credencialRepository,
            UsuarioRepositoryPort    usuarioRepository,
            FileStoragePort          fileStorage) {
        this.credencialRepository = credencialRepository;
        this.usuarioRepository    = usuarioRepository;
        this.fileStorage          = fileStorage;
    }

    public CredencialResponse executar(Long usuarioId, TipoCredencial tipo, MultipartFile arquivo) {
        validarArquivo(arquivo);

        Usuario usuario = usuarioRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));

        if (!usuario.isProfissional()) {
            throw new OperacaoNaoPermitidaException("Apenas profissionais podem enviar credenciais.");
        }

        // Remove credencial anterior se existir (reenvio)
        credencialRepository.buscarPorUsuarioId(usuarioId).ifPresent(c -> {
            if (c.getArquivoUrl() != null) {
                fileStorage.remover(c.getArquivoUrl());
            }
        });

        String url = fileStorage.salvar(arquivo, "credenciais/" + usuarioId);

        Credencial credencial = Credencial.builder()
                .usuarioId(usuarioId)
                .tipo(tipo)
                .arquivoUrl(url)
                .status(StatusCredencial.PENDENTE)
                .criadoEm(LocalDateTime.now())
                .build();

        Credencial salva = credencialRepository.salvar(credencial);

        return toResponse(salva, usuario, null, null);
    }

    private void validarArquivo(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new ArquivoInvalidoException("O arquivo não pode estar vazio.");
        }
        if (arquivo.getSize() > MAX_TAMANHO_BYTES) {
            throw new ArquivoInvalidoException("O arquivo excede o tamanho máximo de 10MB.");
        }
        String contentType = arquivo.getContentType();
        if (contentType == null || !TIPOS_PERMITIDOS.contains(contentType)) {
            throw new ArquivoInvalidoException(
                    "Formato inválido. Envie PDF, JPG ou PNG.");
        }
    }

    static CredencialResponse toResponse(Credencial c, Usuario usuario,
                                         Usuario avaliador, String nomeAvaliador) {
        return CredencialResponse.builder()
                .id(c.getId())
                .usuarioId(c.getUsuarioId())
                .nomeUsuario(usuario != null ? usuario.getNome() : null)
                .emailUsuario(usuario != null ? usuario.getEmail() : null)
                .tipo(c.getTipo())
                .arquivoUrl(c.getArquivoUrl())
                .status(c.getStatus())
                .observacaoAdmin(c.getObservacaoAdmin())
                .criadoEm(c.getCriadoEm())
                .avaliadoEm(c.getAvaliadoEm())
                .avaliadoPorId(c.getAvaliadoPorId())
                .nomeAvaliador(nomeAvaliador)
                .build();
    }
}
