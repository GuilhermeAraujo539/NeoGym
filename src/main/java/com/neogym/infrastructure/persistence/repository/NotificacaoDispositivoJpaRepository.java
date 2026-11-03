
package com.neogym.infrastructure.persistence.repository;

import com.neogym.infrastructure.persistence.entity.NotificacaoDispositivoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificacaoDispositivoJpaRepository
        extends JpaRepository<NotificacaoDispositivoJpaEntity, Long> {

    Optional<NotificacaoDispositivoJpaEntity> findByToken(String token);

    List<NotificacaoDispositivoJpaEntity> findAllByUsuarioId(
            Long usuarioId
    );

    boolean existsByFirebaseUidAndUsuarioId(
            String firebaseUid,
            Long usuarioId
    );

    void deleteByToken(String token);
}
