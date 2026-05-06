package com.neogym.infrastructure.persistence.repository;

import com.neogym.infrastructure.persistence.entity.RefreshTokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenJpaEntity, Long> {

    Optional<RefreshTokenJpaEntity> findByTokenHash(String tokenHash);

    @Modifying
    @Query("UPDATE RefreshTokenJpaEntity r SET r.revogado = true WHERE r.usuario.id = :usuarioId AND r.revogado = false")
    void revogarTodosPorUsuarioId(@Param("usuarioId") Long usuarioId);

    @Modifying
    @Query("DELETE FROM RefreshTokenJpaEntity r WHERE r.expiraEm < :agora")
    void deletarExpirados(@Param("agora") LocalDateTime agora);
}
