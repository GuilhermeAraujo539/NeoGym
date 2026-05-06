package com.neogym.infrastructure.persistence.mapper;

import com.neogym.domain.entity.Aluno;
import com.neogym.infrastructure.persistence.entity.AlunoJpaEntity;
import com.neogym.infrastructure.persistence.entity.UsuarioJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class AlunoMapper {

    public Aluno toDomain(AlunoJpaEntity e) {
        if (e == null) return null;
        return Aluno.builder()
                .id(e.getId())
                .usuarioId(e.getUsuario().getId())
                .academiaId(e.getAcademiaId())
                .peso(e.getPeso())
                .altura(e.getAltura())
                .metaAguaMl(e.getMetaAguaMl())
                .build();
    }

    public AlunoJpaEntity toEntity(Aluno d, UsuarioJpaEntity usuario) {
        if (d == null) return null;
        return AlunoJpaEntity.builder()
                .id(d.getId())
                .usuario(usuario)
                .academiaId(d.getAcademiaId())
                .peso(d.getPeso())
                .altura(d.getAltura())
                .metaAguaMl(d.getMetaAguaMl())
                .build();
    }
}
