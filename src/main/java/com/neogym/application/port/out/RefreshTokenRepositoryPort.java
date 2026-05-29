package com.neogym.application.port.out;

import com.neogym.domain.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepositoryPort {

    RefreshToken salvar(RefreshToken token);

    Optional<RefreshToken> buscarPorHash(String tokenHash);

    void revogarTodosPorUsuarioId(Long usuarioId);

    void removerExpirados();
}
