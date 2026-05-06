package com.neogym.infrastructure.persistence.mapper;

import com.neogym.domain.entity.Usuario;
import com.neogym.domain.enums.TipoUsuario;
import com.neogym.infrastructure.persistence.entity.UsuarioJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toDomain(UsuarioJpaEntity e) {
        if (e == null) return null;
        return Usuario.builder()
                .id(e.getId())
                .nome(e.getNome())
                .email(e.getEmail())
                .senhaHash(e.getSenhaHash())
                .tipo(TipoUsuario.valueOf(e.getTipo()))
                .ativo(e.isAtivo())
                .criadoEm(e.getCriadoEm())
                .build();
    }

    public UsuarioJpaEntity toEntity(Usuario d) {
        if (d == null) return null;
        return UsuarioJpaEntity.builder()
                .id(d.getId())
                .nome(d.getNome())
                .email(d.getEmail())
                .senhaHash(d.getSenhaHash())
                .tipo(d.getTipo().name())
                .ativo(d.isAtivo())
                .criadoEm(d.getCriadoEm())
                .build();
    }
}
