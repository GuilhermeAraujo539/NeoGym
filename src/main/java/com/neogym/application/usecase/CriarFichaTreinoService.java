package com.neogym.application.usecase;

import com.neogym.application.dto.request.CriarFichaTreinoRequest;
import com.neogym.application.dto.response.FichaTreinoResponse;
import com.neogym.application.port.in.CriarFichaTreinoUseCase;
import com.neogym.application.port.out.AlunoRepositoryPort;
import com.neogym.application.port.out.FichaTreinoRepositoryPort;
import com.neogym.application.port.out.PersonalAlunoRepositoryPort;
import com.neogym.application.port.out.PersonalRepositoryPort;
import com.neogym.domain.entity.FichaTreino;
import com.neogym.domain.entity.FichaTreinoExercicio;

import java.util.List;

public class CriarFichaTreinoService
        implements CriarFichaTreinoUseCase {

    private final PersonalRepositoryPort personalRepository;
    private final AlunoRepositoryPort alunoRepository;
    private final PersonalAlunoRepositoryPort personalAlunoRepository;
    private final FichaTreinoRepositoryPort fichaTreinoRepository;

    public CriarFichaTreinoService(
            PersonalRepositoryPort personalRepository,
            AlunoRepositoryPort alunoRepository,
            PersonalAlunoRepositoryPort personalAlunoRepository,
            FichaTreinoRepositoryPort fichaTreinoRepository
    ) {
        this.personalRepository = personalRepository;
        this.alunoRepository = alunoRepository;
        this.personalAlunoRepository =
                personalAlunoRepository;
        this.fichaTreinoRepository =
                fichaTreinoRepository;
    }

    @Override
    public FichaTreinoResponse executar(
            Long usuarioId,
            CriarFichaTreinoRequest request
    ) {

        var personal =
                personalRepository
                        .buscarPorUsuarioId(usuarioId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Personal não encontrado"
                                )
                        );

        if (!personal.isAprovado()) {
            throw new RuntimeException(
                    "Personal não está aprovado"
            );
        }

        alunoRepository
                .buscarPorId(request.getAlunoId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Aluno não encontrado"
                        )
                );

        boolean possuiVinculo =
                personalAlunoRepository.existeVinculo(
                        personal.getId(),
                        request.getAlunoId()
                );

        if (!possuiVinculo) {
            throw new RuntimeException(
                    "Personal não possui vínculo com este aluno"
            );
        }

        List<FichaTreinoExercicio> exercicios =
                java.util.stream.IntStream
                        .range(
                                0,
                                request
                                        .getExercicios()
                                        .size()
                        )
                        .mapToObj(index -> {
                            var item =
                                    request
                                            .getExercicios()
                                            .get(index);

                            return FichaTreinoExercicio.criar(
                                    item.getNome(),
                                    item.getSeries(),
                                    item.getRepeticoes(),
                                    index + 1
                            );
                        })
                        .toList();

        FichaTreino ficha =
                FichaTreino.criar(
                        request.getAlunoId(),
                        personal.getId(),
                        request.getNome(),
                        request.getObjetivo(),
                        request.getDuracao(),
                        exercicios
                );

        FichaTreino salva =
                fichaTreinoRepository.salvar(ficha);

        return toResponse(salva);
    }

    private FichaTreinoResponse toResponse(
            FichaTreino ficha
    ) {
        return FichaTreinoResponse.builder()
                .id(ficha.getId())
                .alunoId(ficha.getAlunoId())
                .personalId(ficha.getPersonalId())
                .nome(ficha.getNome())
                .objetivo(ficha.getObjetivo())
                .duracao(ficha.getDuracao())
                .exercicios(
                        ficha.getExercicios()
                                .stream()
                                .map(e ->
                                        FichaTreinoResponse
                                                .ExercicioResponse
                                                .builder()
                                                .id(e.getId())
                                                .nome(e.getNome())
                                                .series(e.getSeries())
                                                .repeticoes(e.getRepeticoes())
                                                .ordem(e.getOrdem())
                                                .build()
                                )
                                .toList()
                )
                .build();
    }
}