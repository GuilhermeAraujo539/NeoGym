package com.neogym.infrastructure.persistence.repository;

import com.neogym.infrastructure.persistence.entity.AgendamentoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendamentoJpaRepository
        extends JpaRepository<AgendamentoJpaEntity, Long> {

    List<AgendamentoJpaEntity> findByAlunoIdOrderByDataHoraAsc(
            Long alunoId
    );

    List<AgendamentoJpaEntity> findByPersonalIdOrderByDataHoraAsc(
            Long personalId
    );

    List<AgendamentoJpaEntity> findByNutricionistaIdOrderByDataHoraAsc(
            Long nutricionistaId
    );
}