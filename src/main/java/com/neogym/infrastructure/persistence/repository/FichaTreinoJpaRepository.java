package com.neogym.infrastructure.persistence.repository;

import com.neogym.infrastructure.persistence.entity.FichaTreinoJpaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FichaTreinoJpaRepository
        extends JpaRepository<FichaTreinoJpaEntity, Long> {

    @EntityGraph(attributePaths = "exercicios")
    List<FichaTreinoJpaEntity>
    findByAlunoIdOrderByCriadaEmDesc(Long alunoId);
}