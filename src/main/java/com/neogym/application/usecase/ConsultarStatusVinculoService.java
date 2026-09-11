package com.neogym.application.usecase;

import com.neogym.application.dto.response.StatusVinculoResponse;
import com.neogym.application.port.in.ConsultarStatusVinculoUseCase;
import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.application.port.out.VinculoProfissionalRepositoryPort;
import com.neogym.domain.entity.Usuario;
import com.neogym.domain.enums.TipoUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultarStatusVinculoService
        implements ConsultarStatusVinculoUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final VinculoProfissionalRepositoryPort vinculoRepository;

    @Override
    public StatusVinculoResponse executar(
            String email,
            Long outroUsuarioId
    ) {

        Usuario usuario = usuarioRepository
                .buscarPorEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Usuário não encontrado")
                );

        if (usuario.getTipo() != TipoUsuario.ALUNO
                && usuario.getTipo() != TipoUsuario.PERSONAL
                && usuario.getTipo() != TipoUsuario.NUTRICIONISTA) {

            throw new IllegalArgumentException(
                    "Usuário não pode consultar vínculos"
            );
        }

        return vinculoRepository
                .buscarPorUsuarios(usuario.getId(), outroUsuarioId)
                .map(vinculo -> StatusVinculoResponse.builder()
                        .existe(true)
                        .vinculoId(vinculo.getId())
                        .status(vinculo.getStatus())
                        .build()
                )
                .orElseGet(() -> StatusVinculoResponse.builder()
                        .existe(false)
                        .vinculoId(null)
                        .status(null)
                        .build()
                );
    }
}