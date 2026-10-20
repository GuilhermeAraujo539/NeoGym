package com.neogym.infrastructure.persistence.repository;

import com.neogym.infrastructure.persistence.entity.UsuarioFotoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioFotoJpaRepository
        extends JpaRepository<UsuarioFotoJpaEntity, Long> {
}