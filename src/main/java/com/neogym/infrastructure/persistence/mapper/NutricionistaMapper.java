package com.neogym.infrastructure.persistence.mapper;

import com.neogym.domain.entity.Nutricionista;
import com.neogym.domain.enums.StatusCredencial;
import com.neogym.infrastructure.persistence.entity.NutricionistaJpaEntity;
import com.neogym.infrastructure.persistence.entity.UsuarioJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class NutricionistaMapper {

    public Nutricionista toDomain(NutricionistaJpaEntity e) {
        if (e == null) return null;
        return Nutricionista.builder()
                .id(e.getId())
                .usuarioId(e.getUsuario().getId())
                .crn(e.getCrn())
                .estadoCrn(e.getEstadoCrn())
                .statusCrn(StatusCredencial.valueOf(e.getStatusCrn()))
                .build();
    }

    public NutricionistaJpaEntity toEntity(Nutricionista d, UsuarioJpaEntity usuario) {
        if (d == null) return null;
        return NutricionistaJpaEntity.builder()
                .id(d.getId())
                .usuario(usuario)
                .crn(d.getCrn())
                .estadoCrn(d.getEstadoCrn())
                .statusCrn(d.getStatusCrn().name())
                .build();
    }
}
