package com.neogym.application.port.out;

import com.neogym.domain.entity.Usuario;

public interface AccessTokenPort {

    String gerar(Usuario usuario);

    String extrairEmail(String token);

    boolean validar(String token);

    long expiracaoMs();
}
