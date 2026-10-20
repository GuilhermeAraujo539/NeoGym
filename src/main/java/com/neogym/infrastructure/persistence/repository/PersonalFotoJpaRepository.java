package com.neogym.infrastructure.persistence.repository;

import com.neogym.infrastructure.persistence.entity.PersonalFotoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonalFotoJpaRepository
        extends JpaRepository<PersonalFotoJpaEntity, Long> {

    List<PersonalFotoJpaEntity> findByPersonal_IdOrderByCriadoEmDesc(
            Long personalId
    );

    Optional<PersonalFotoJpaEntity> findByIdAndPersonal_Id(
            Long fotoId,
            Long personalId
    );

    long countByPersonal_Id(Long personalId);
}