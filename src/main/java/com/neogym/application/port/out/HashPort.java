package com.neogym.application.port.out;

public interface HashPort {

    String sha256(String valor);

    String gerarTokenAleatorio();
}
