package com.neogym.infrastructure.persistence.repository;

import com.neogym.infrastructure.persistence.entity.CredencialJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CredencialJpaRepository extends JpaRepository<CredencialJpaEntity, Long> {

    @Query("SELECT c FROM CredencialJpaEntity c WHERE c.usuario.id = :usuarioId ORDER BY c.criadoEm DESC")
    Optional<CredencialJpaEntity> findTopByUsuarioId(@Param("usuarioId") Long usuarioId);

    Page<CredencialJpaEntity> findByStatus(String status, Pageable pageable);

    long countByStatus(String status);

    @org.springframework.data.jpa.repository.Query("""
        SELECT c FROM CredencialJpaEntity c
        WHERE c.usuario.id = :usuarioId
        ORDER BY c.criadoEm DESC
        """)
    org.springframework.data.domain.Page<CredencialJpaEntity> findByUsuarioIdPaginado(
            @org.springframework.data.repository.query.Param("usuarioId") Long usuarioId,
            org.springframework.data.domain.Pageable pageable);
}
