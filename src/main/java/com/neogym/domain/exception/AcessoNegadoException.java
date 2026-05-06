package com.neogym.domain.exception;

public class AcessoNegadoException extends NeoGymException {
    public AcessoNegadoException() {
        super("Acesso negado.");
    }
    public AcessoNegadoException(String mensagem) {
        super(mensagem);
    }
}
