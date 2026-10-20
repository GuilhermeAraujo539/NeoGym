package com.neogym.application.port.out;

public record FotoUsuario(
        byte[] dados,
        String contentType
) {
}