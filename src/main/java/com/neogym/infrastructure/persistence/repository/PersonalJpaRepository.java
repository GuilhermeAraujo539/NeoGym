package com.neogym.infrastructure.persistence.repository;

import com.neogym.infrastructure.persistence.entity.PersonalJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PersonalJpaRepository extends JpaRepository<PersonalJpaEntity, Long> {

    @Query("SELECT p FROM PersonalJpaEntity p WHERE p.usuario.id = :usuarioId")
    Optional<PersonalJpaEntity> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}
