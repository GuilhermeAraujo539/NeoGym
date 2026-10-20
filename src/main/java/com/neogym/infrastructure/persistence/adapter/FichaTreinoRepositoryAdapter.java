package com.neogym.infrastructure.persistence.adapter;

import com.neogym.application.port.out.FichaTreinoRepositoryPort;
import com.neogym.domain.entity.FichaTreino;
import com.neogym.domain.entity.FichaTreinoExercicio;
import com.neogym.infrastructure.persistence.entity.FichaTreinoExercicioJpaEntity;
import com.neogym.infrastructure.persistence.entity.FichaTreinoJpaEntity;
import com.neogym.infrastructure.persistence.repository.FichaTreinoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FichaTreinoRepositoryAdapter
        implements FichaTreinoRepositoryPort {

    private final FichaTreinoJpaRepository repository;

    @Override
    public FichaTreino salvar(FichaTreino ficha) {

        FichaTreinoJpaEntity entity =
                new FichaTreinoJpaEntity(
                        ficha.getAlunoId(),
                        ficha.getPersonalId(),
                        ficha.getNome(),
                        ficha.getObjetivo(),
                        ficha.getDuracao(),
                        ficha.getCriadaEm()
                );

        for (FichaTreinoExercicio exercicio
                : ficha.getExercicios()) {

            entity.adicionarExercicio(
                    new FichaTreinoExercicioJpaEntity(
                            exercicio.getNome(),
                            exercicio.getSeries(),
                            exercicio.getRepeticoes(),
                            exercicio.getOrdem()
                    )
            );
        }

        FichaTreinoJpaEntity saved =
                repository.save(entity);

        return toDomain(saved);
    }

    private FichaTreino toDomain(
            FichaTreinoJpaEntity entity
    ) {
        var exercicios = entity.getExercicios()
                .stream()
                .map(e ->
                        new FichaTreinoExercicio(
                                e.getId(),
                                e.getNome(),
                                e.getSeries(),
                                e.getRepeticoes(),
                                e.getOrdem()
                        )
                )
                .toList();

        return new FichaTreino(
                entity.getId(),
                entity.getAlunoId(),
                entity.getPersonalId(),
                entity.getNome(),
                entity.getObjetivo(),
                entity.getDuracao(),
                entity.getCriadaEm(),
                exercicios
        );
    }

    @Override
    public List<FichaTreino> buscarPorAlunoId(Long alunoId) {

        return repository
                .findByAlunoIdOrderByCriadaEmDesc(alunoId)
                .stream()
                .map(this::toDomain)
                .toList();
    }
}