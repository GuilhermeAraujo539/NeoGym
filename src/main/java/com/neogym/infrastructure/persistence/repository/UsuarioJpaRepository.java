package com.neogym.infrastructure.persistence.repository;

import com.neogym.infrastructure.persistence.entity.UsuarioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioJpaEntity, Long> {
    Optional<UsuarioJpaEntity> findByEmail(String email);
    boolean existsByEmail(String email);

    long countByTipo(String tipo);

    long countByAtivo(boolean ativo);

    @org.springframework.data.jpa.repository.Query("""
        SELECT u FROM UsuarioJpaEntity u
        WHERE (:tipo IS NULL OR u.tipo = :tipo)
          AND (:ativo IS NULL OR u.ativo = :ativo)
        ORDER BY u.criadoEm DESC
        """)
    org.springframework.data.domain.Page<UsuarioJpaEntity> findComFiltros(
            @org.springframework.data.repository.query.Param("tipo") String tipo,
            @org.springframework.data.repository.query.Param("ativo") Boolean ativo,
            org.springframework.data.domain.Pageable pageable);
}
