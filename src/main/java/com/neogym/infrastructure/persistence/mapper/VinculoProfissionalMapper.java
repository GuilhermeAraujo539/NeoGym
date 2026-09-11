package com.neogym.infrastructure.persistence.mapper;

import com.neogym.domain.entity.VinculoProfissional;
import com.neogym.infrastructure.persistence.entity.VinculoProfissionalJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class VinculoProfissionalMapper {

    public VinculoProfissional toDomain(VinculoProfissionalJpaEntity entity) {

        if (entity == null) {
            return null;
        }

        return VinculoProfissional.builder()
                .id(entity.getId())
                .alunoId(entity.getAluno().getId())
                .profissionalUsuarioId(entity.getProfissional().getId())
                .tipoProfissional(entity.getTipoProfissional())
                .status(entity.getStatus())
                .criadoEm(entity.getCriadoEm())
                .atualizadoEm(entity.getAtualizadoEm())
                .build();
    }
}