package com.neogym.infrastructure.persistence.repository;

import com.neogym.infrastructure.persistence.entity.AlunoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AlunoJpaRepository extends JpaRepository<AlunoJpaEntity, Long> {

    @Query("SELECT a FROM AlunoJpaEntity a WHERE a.usuario.id = :usuarioId")
    Optional<AlunoJpaEntity> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}
