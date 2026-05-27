package com.neogym.infrastructure.web.controller;

import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.domain.exception.UsuarioNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Helper que resolve e-mail (extraído do JWT) para Long id.
 * Compartilhado entre controllers que precisam do usuário logado.
 */
@Component
@RequiredArgsConstructor
public class UsuarioResolverHelper {

    private final UsuarioRepositoryPort usuarioRepository;

    public Long resolverId(String email) {
        return usuarioRepository.buscarPorEmail(email)
                .map(u -> u.getId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException(email));
    }
}
