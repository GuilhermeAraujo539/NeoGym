package com.neogym.domain.exception;

public class UsuarioNaoEncontradoException extends NeoGymException {
    public UsuarioNaoEncontradoException(String email) {
        super("Usuário não encontrado: " + email);
    }
    public UsuarioNaoEncontradoException(Long id) {
        super("Usuário não encontrado com id: " + id);
    }
}
