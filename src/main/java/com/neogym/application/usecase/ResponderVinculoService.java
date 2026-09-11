package com.neogym.application.usecase;

import com.neogym.application.port.in.ResponderVinculoUseCase;
import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.application.port.out.VinculoProfissionalRepositoryPort;
import com.neogym.domain.entity.Usuario;
import com.neogym.domain.entity.VinculoProfissional;
import com.neogym.domain.enums.StatusVinculo;
import com.neogym.domain.enums.TipoUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ResponderVinculoService
        implements ResponderVinculoUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final VinculoProfissionalRepositoryPort vinculoRepository;

    @Override
    public VinculoProfissional aceitar(
            String email,
            Long vinculoId
    ) {
        return alterarStatus(
                email,
                vinculoId,
                StatusVinculo.ACEITO
        );
    }

    @Override
    public VinculoProfissional recusar(
            String email,
            Long vinculoId
    ) {
        return alterarStatus(
                email,
                vinculoId,
                StatusVinculo.RECUSADO
        );
    }

    private VinculoProfissional alterarStatus(
            String email,
            Long vinculoId,
            StatusVinculo novoStatus
    ) {

        Usuario profissional = usuarioRepository
                .buscarPorEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuário não encontrado"
                        )
                );

        if (profissional.getTipo() != TipoUsuario.PERSONAL
                && profissional.getTipo() != TipoUsuario.NUTRICIONISTA) {

            throw new IllegalArgumentException(
                    "Apenas profissionais podem responder solicitações"
            );
        }

        VinculoProfissional vinculo =
                vinculoRepository.buscarPorIdEProfissional(
                        vinculoId,
                        profissional.getId()
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "Solicitação não encontrada"
                        )
                );

        if (vinculo.getStatus() != StatusVinculo.PENDENTE) {
            throw new IllegalArgumentException(
                    "Esta solicitação já foi respondida"
            );
        }

        vinculo.alterarStatus(novoStatus);

        return vinculoRepository.atualizar(vinculo);
    }
}