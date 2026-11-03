package com.neogym.infrastructure.persistence.repository;

import com.neogym.infrastructure.persistence.entity.PersonalFotoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonalFotoJpaRepository
        extends JpaRepository<PersonalFotoJpaEntity, Long> {

    List<PersonalFotoJpaEntity> findByUsuario_IdOrderByCriadoEmDesc(
            Long usuarioId);

    Optional<PersonalFotoJpaEntity> findByIdAndUsuario_Id(
            Long fotoId,
            Long usuarioId);

    long countByUsuario_Id(Long usuarioId);

    void deleteByIdAndUsuario_Id(Long fotoId, Long usuarioId);
}