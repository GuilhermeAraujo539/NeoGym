package com.neogym.application.dto.request;

public record RegistrarDispositivoRequest(
        String token,
        String plataforma,
        String firebaseIdToken
) {
}