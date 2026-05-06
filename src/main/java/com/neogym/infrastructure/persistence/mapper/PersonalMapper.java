package com.neogym.infrastructure.persistence.mapper;

import com.neogym.domain.entity.Personal;
import com.neogym.domain.enums.StatusCredencial;
import com.neogym.infrastructure.persistence.entity.PersonalJpaEntity;
import com.neogym.infrastructure.persistence.entity.UsuarioJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PersonalMapper {

    public Personal toDomain(PersonalJpaEntity e) {
        if (e == null) return null;
        return Personal.builder()
                .id(e.getId())
                .usuarioId(e.getUsuario().getId())
                .cref(e.getCref())
                .estadoCref(e.getEstadoCref())
                .statusCref(StatusCredencial.valueOf(e.getStatusCref()))
                .build();
    }

    public PersonalJpaEntity toEntity(Personal d, UsuarioJpaEntity usuario) {
        if (d == null) return null;
        return PersonalJpaEntity.builder()
                .id(d.getId())
                .usuario(usuario)
                .cref(d.getCref())
                .estadoCref(d.getEstadoCref())
                .statusCref(d.getStatusCref().name())
                .build();
    }
}
