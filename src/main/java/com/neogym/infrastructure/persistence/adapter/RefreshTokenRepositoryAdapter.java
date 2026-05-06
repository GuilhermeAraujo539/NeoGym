package com.neogym.infrastructure.persistence.adapter;

import com.neogym.application.port.out.RefreshTokenRepositoryPort;
import com.neogym.domain.entity.RefreshToken;
import com.neogym.infrastructure.persistence.entity.UsuarioJpaEntity;
import com.neogym.infrastructure.persistence.mapper.RefreshTokenMapper;
import com.neogym.infrastructure.persistence.repository.RefreshTokenJpaRepository;
import com.neogym.infrastructure.persistence.repository.UsuarioJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepositoryPort {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;
    private final UsuarioJpaRepository      usuarioJpaRepository;
    private final RefreshTokenMapper        mapper;

    @Override
    public RefreshToken salvar(RefreshToken token) {
        UsuarioJpaEntity usuario = usuarioJpaRepository.findById(token.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado ao salvar refresh token: " + token.getUsuarioId()));
        return mapper.toDomain(refreshTokenJpaRepository.save(mapper.toEntity(token, usuario)));
    }

    @Override
    public Optional<RefreshToken> buscarPorHash(String tokenHash) {
        return refreshTokenJpaRepository.findByTokenHash(tokenHash).map(mapper::toDomain);
    }

    @Override
    public void revogarTodosPorUsuarioId(Long usuarioId) {
        refreshTokenJpaRepository.revogarTodosPorUsuarioId(usuarioId);
    }

    @Override
    public void removerExpirados() {
        refreshTokenJpaRepository.deletarExpirados(LocalDateTime.now());
    }
}
