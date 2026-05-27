package com.neogym.domain.exception;

public class CredencialNaoEncontradaException extends NeoGymException {
    public CredencialNaoEncontradaException(Long id) {
        super("Credencial não encontrada com id: " + id);
    }
}
