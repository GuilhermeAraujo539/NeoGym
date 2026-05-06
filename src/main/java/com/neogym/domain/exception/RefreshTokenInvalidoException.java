package com.neogym.domain.exception;

public class RefreshTokenInvalidoException extends NeoGymException {
    public RefreshTokenInvalidoException() {
        super("Refresh token inválido, expirado ou já utilizado. Faça login novamente.");
    }
}
