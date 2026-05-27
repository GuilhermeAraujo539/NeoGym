package com.neogym.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.neogym.infrastructure.persistence.entity.PersonalAcademiaJpaEntity;

public interface PersonalAcademiaJpaRepository extends JpaRepository<PersonalAcademiaJpaEntity, Long> {
    long countByStatus(String status);
}
