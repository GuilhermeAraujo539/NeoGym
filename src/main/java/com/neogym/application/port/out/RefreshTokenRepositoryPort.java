package com.neogym.application.port.out;

import com.neogym.domain.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepositoryPort {

    RefreshToken salvar(RefreshToken token);

    /** Busca pelo hash SHA-256 do token bruto. */
    Optional<RefreshToken> buscarPorHash(String tokenHash);

    /** Revoga (invalida) todos os refresh tokens do usuário — usado no logout total. */
    void revogarTodosPorUsuarioId(Long usuarioId);

    /** Remove tokens expirados (chamado por job de limpeza). */
    void removerExpirados();
}
