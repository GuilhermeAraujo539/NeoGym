package com.neogym.application.usecase;

import com.neogym.application.dto.request.AtualizarPerfilRequest;
import com.neogym.application.dto.response.PerfilResponse;
import com.neogym.application.port.in.AtualizarPerfilUseCase;
import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.domain.entity.Usuario;

public class AtualizarPerfilService
        implements AtualizarPerfilUseCase {

    private final UsuarioRepositoryPort usuarioRepository;

    public AtualizarPerfilService(
            UsuarioRepositoryPort usuarioRepository
    ) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public PerfilResponse executar(
            Long usuarioId,
            AtualizarPerfilRequest request
    ) {

        Usuario usuario = usuarioRepository
                .buscarPorId(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuário não encontrado"
                        )
                );

        if (!usuario.getEmail().equalsIgnoreCase(request.getEmail())
                && usuarioRepository.existePorEmail(request.getEmail())) {

            throw new RuntimeException(
                    "E-mail já está em uso"
            );
        }

        Usuario atualizado = Usuario.builder()
                .id(usuario.getId())
                .nome(request.getNome().trim())
                .email(request.getEmail().trim())
                .senhaHash(usuario.getSenhaHash())
                .tipo(usuario.getTipo())
                .ativo(usuario.isAtivo())
                .criadoEm(usuario.getCriadoEm())
                .build();

        Usuario salvo = usuarioRepository.salvar(atualizado);

        return PerfilResponse.builder()
                .id(salvo.getId())
                .nome(salvo.getNome())
                .email(salvo.getEmail())
                .tipo(salvo.getTipo())
                .build();
    }
}