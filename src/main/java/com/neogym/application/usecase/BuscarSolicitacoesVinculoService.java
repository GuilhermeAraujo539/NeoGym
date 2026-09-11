package com.neogym.application.usecase;

import com.neogym.application.port.in.BuscarSolicitacoesVinculoUseCase;
import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.application.port.out.VinculoProfissionalRepositoryPort;
import com.neogym.domain.entity.Usuario;
import com.neogym.domain.entity.VinculoProfissional;
import com.neogym.domain.enums.TipoUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarSolicitacoesVinculoService
        implements BuscarSolicitacoesVinculoUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final VinculoProfissionalRepositoryPort vinculoRepository;

    @Override
    public List<VinculoProfissional> executar(String email) {

        Usuario profissional = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Usuário não encontrado")
                );

        if (profissional.getTipo() != TipoUsuario.PERSONAL
                && profissional.getTipo() != TipoUsuario.NUTRICIONISTA) {

            throw new IllegalArgumentException(
                    "Apenas profissionais podem consultar solicitações"
            );
        }

        return vinculoRepository.buscarSolicitacoesPendentes(
                profissional.getId()
        );
    }
}