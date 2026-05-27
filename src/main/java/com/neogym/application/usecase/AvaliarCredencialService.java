package com.neogym.application.usecase;

import com.neogym.application.dto.request.AvaliarCredencialRequest;
import com.neogym.application.dto.response.CredencialResponse;
import com.neogym.application.port.out.CredencialRepositoryPort;
import com.neogym.application.port.out.NutricionistaRepositoryPort;
import com.neogym.application.port.out.PersonalRepositoryPort;
import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.domain.entity.Credencial;
import com.neogym.domain.entity.Nutricionista;
import com.neogym.domain.entity.Personal;
import com.neogym.domain.entity.Usuario;
import com.neogym.domain.enums.StatusCredencial;
import com.neogym.domain.enums.TipoCredencial;
import com.neogym.domain.exception.CredencialNaoEncontradaException;
import com.neogym.domain.exception.OperacaoNaoPermitidaException;
import com.neogym.domain.exception.UsuarioNaoEncontradoException;

import java.time.LocalDateTime;

/**
 * Caso de uso: admin avalia uma credencial (APROVADO ou REJEITADO).
 *
 * Ao aprovar, sincroniza o status no perfil de Personal ou Nutricionista,
 * tornando o profissional visível nas buscas.
 * Ao rejeitar, registra a observação para que o profissional saiba o motivo.
 */
public class AvaliarCredencialService {

    private final CredencialRepositoryPort    credencialRepository;
    private final UsuarioRepositoryPort       usuarioRepository;
    private final PersonalRepositoryPort      personalRepository;
    private final NutricionistaRepositoryPort nutricionistaRepository;

    public AvaliarCredencialService(
            CredencialRepositoryPort    credencialRepository,
            UsuarioRepositoryPort       usuarioRepository,
            PersonalRepositoryPort      personalRepository,
            NutricionistaRepositoryPort nutricionistaRepository) {
        this.credencialRepository    = credencialRepository;
        this.usuarioRepository       = usuarioRepository;
        this.personalRepository      = personalRepository;
        this.nutricionistaRepository = nutricionistaRepository;
    }

    public CredencialResponse executar(Long credencialId, Long adminId,
                                       AvaliarCredencialRequest req) {

        if (req.getStatus() == StatusCredencial.PENDENTE) {
            throw new OperacaoNaoPermitidaException(
                    "Status PENDENTE não é uma avaliação válida.");
        }

        if (req.getStatus() == StatusCredencial.REJEITADO &&
                (req.getObservacao() == null || req.getObservacao().isBlank())) {
            throw new OperacaoNaoPermitidaException(
                    "Informe a observação ao rejeitar uma credencial.");
        }

        Credencial credencial = credencialRepository.buscarPorId(credencialId)
                .orElseThrow(() -> new CredencialNaoEncontradaException(credencialId));

        if (!credencial.isPendente()) {
            throw new OperacaoNaoPermitidaException(
                    "Esta credencial já foi avaliada e não pode ser alterada.");
        }

        Credencial atualizada = Credencial.builder()
                .id(credencial.getId())
                .usuarioId(credencial.getUsuarioId())
                .tipo(credencial.getTipo())
                .arquivoUrl(credencial.getArquivoUrl())
                .status(req.getStatus())
                .observacaoAdmin(req.getObservacao())
                .criadoEm(credencial.getCriadoEm())
                .avaliadoEm(LocalDateTime.now())
                .avaliadoPorId(adminId)
                .build();

        credencialRepository.salvar(atualizada);

        // Sincroniza o status no perfil do profissional
        sincronizarStatusProfissional(credencial.getUsuarioId(),
                credencial.getTipo(), req.getStatus());

        Usuario usuario = usuarioRepository.buscarPorId(credencial.getUsuarioId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException(credencial.getUsuarioId()));

        Usuario admin = usuarioRepository.buscarPorId(adminId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(adminId));

        return EnviarCredencialService.toResponse(atualizada, usuario, admin, admin.getNome());
    }

    private void sincronizarStatusProfissional(Long usuarioId,
                                               TipoCredencial tipo,
                                               StatusCredencial novoStatus) {
        if (tipo == TipoCredencial.CREF) {
            personalRepository.buscarPorUsuarioId(usuarioId).ifPresent(p -> {
                Personal atualizado = Personal.builder()
                        .id(p.getId())
                        .usuarioId(p.getUsuarioId())
                        .cref(p.getCref())
                        .estadoCref(p.getEstadoCref())
                        .statusCref(novoStatus)
                        .build();
                personalRepository.salvar(atualizado);
            });
        } else {
            nutricionistaRepository.buscarPorUsuarioId(usuarioId).ifPresent(n -> {
                Nutricionista atualizada = Nutricionista.builder()
                        .id(n.getId())
                        .usuarioId(n.getUsuarioId())
                        .crn(n.getCrn())
                        .estadoCrn(n.getEstadoCrn())
                        .statusCrn(novoStatus)
                        .build();
                nutricionistaRepository.salvar(atualizada);
            });
        }
    }
}
