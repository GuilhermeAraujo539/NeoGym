package com.neogym.domain.exception;

public class CredenciaisInvalidasException extends NeoGymException {
    public CredenciaisInvalidasException() {
        super("E-mail ou senha inválidos.");
    }
}
