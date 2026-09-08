package com.neogym.infrastructure.persistence.adapter;

import com.neogym.application.port.out.PersonalAlunoRepositoryPort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

@Component
public class PersonalAlunoRepositoryAdapter
        implements PersonalAlunoRepositoryPort {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean existeVinculo(Long personalId, Long alunoId) {

        Number resultado = (Number) entityManager
                .createNativeQuery("""
                SELECT COUNT(*)
                FROM ficha_treino
                WHERE personal_id = ?1
                  AND aluno_id = ?2
                """)
                .setParameter(1, personalId)
                .setParameter(2, alunoId)
                .getSingleResult();

        return resultado.longValue() > 0;
    }
}