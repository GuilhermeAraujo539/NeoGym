package com.neogym.infrastructure.persistence.repository;

import com.neogym.infrastructure.persistence.entity.NutricionistaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NutricionistaJpaRepository extends JpaRepository<NutricionistaJpaEntity, Long> {

    @Query("SELECT n FROM NutricionistaJpaEntity n WHERE n.usuario.id = :usuarioId")
    Optional<NutricionistaJpaEntity> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}
