package com.neogym.domain.exception;

public class UsuarioJaExisteException extends NeoGymException {
    public UsuarioJaExisteException(String email) {
        super("Já existe um usuário cadastrado com o e-mail: " + email);
    }
}
