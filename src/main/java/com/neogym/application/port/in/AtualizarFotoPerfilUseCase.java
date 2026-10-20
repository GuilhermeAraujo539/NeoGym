package com.neogym.application.port.in;

public interface AtualizarFotoPerfilUseCase {

    void executar(
            Long usuarioId,
            byte[] dados,
            String contentType
    );
}