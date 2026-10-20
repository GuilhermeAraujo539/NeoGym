package com.neogym.infrastructure.security.service;

import com.neogym.application.port.in.AtualizarFotoPerfilUseCase;
import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.application.port.out.UsuarioFotoRepositoryPort;

public class AtualizarFotoPerfilService
        implements AtualizarFotoPerfilUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final UsuarioFotoRepositoryPort fotoRepository;

    public AtualizarFotoPerfilService(
            UsuarioRepositoryPort usuarioRepository,
            UsuarioFotoRepositoryPort fotoRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.fotoRepository = fotoRepository;
    }

    @Override
    public void executar(
            Long usuarioId,
            byte[] dados,
            String contentType
    ) {

        usuarioRepository
                .buscarPorId(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuário não encontrado"
                        )
                );

        if (dados == null || dados.length == 0) {
            throw new RuntimeException(
                    "Imagem inválida"
            );
        }

        fotoRepository.salvar(
                usuarioId,
                dados,
                contentType
        );
    }
}