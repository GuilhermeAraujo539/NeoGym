package com.neogym.infrastructure.persistence.mapper;

import com.neogym.domain.entity.RefreshToken;
import com.neogym.infrastructure.persistence.entity.RefreshTokenJpaEntity;
import com.neogym.infrastructure.persistence.entity.UsuarioJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenMapper {

    public RefreshToken toDomain(RefreshTokenJpaEntity e) {
        if (e == null) return null;
        return RefreshToken.builder()
                .id(e.getId())
                .usuarioId(e.getUsuario().getId())
                .tokenHash(e.getTokenHash())
                .expiraEm(e.getExpiraEm())
                .revogado(e.isRevogado())
                .criadoEm(e.getCriadoEm())
                .ipOrigem(e.getIpOrigem())
                .userAgent(e.getUserAgent())
                .build();
    }

    public RefreshTokenJpaEntity toEntity(RefreshToken d, UsuarioJpaEntity usuario) {
        if (d == null) return null;
        return RefreshTokenJpaEntity.builder()
                .id(d.getId())
                .usuario(usuario)
                .tokenHash(d.getTokenHash())
                .expiraEm(d.getExpiraEm())
                .revogado(d.isRevogado())
                .criadoEm(d.getCriadoEm())
                .ipOrigem(d.getIpOrigem())
                .userAgent(d.getUserAgent())
                .build();
    }
}
