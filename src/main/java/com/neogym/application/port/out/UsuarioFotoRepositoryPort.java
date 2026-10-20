package com.neogym.application.port.out;

import java.util.Optional;

public interface UsuarioFotoRepositoryPort {

    void salvar(
            Long usuarioId,
            byte[] dados,
            String contentType
    );

    Optional<FotoUsuario> buscar(Long usuarioId);
}