package com.neogym.application.usecase;

import com.neogym.application.dto.response.FichaTreinoResponse;
import com.neogym.application.port.in.ListarFichasTreinoUseCase;
import com.neogym.application.port.out.AlunoRepositoryPort;
import com.neogym.application.port.out.FichaTreinoRepositoryPort;
import com.neogym.application.port.out.PersonalRepositoryPort;
import com.neogym.domain.entity.FichaTreino;

import java.util.List;
import java.util.Objects;

public class ListarFichasTreinoService
        implements ListarFichasTreinoUseCase {

    private final FichaTreinoRepositoryPort fichaTreinoRepository;
    private final AlunoRepositoryPort alunoRepository;
    private final PersonalRepositoryPort personalRepository;

    public ListarFichasTreinoService(
            FichaTreinoRepositoryPort fichaTreinoRepository,
            AlunoRepositoryPort alunoRepository,
            PersonalRepositoryPort personalRepository
    ) {
        this.fichaTreinoRepository = fichaTreinoRepository;
        this.alunoRepository = alunoRepository;
        this.personalRepository = personalRepository;
    }

    @Override
    public List<FichaTreinoResponse> executar(Long alunoId) {
        return fichaTreinoRepository
                .buscarPorAlunoId(alunoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Consulta utilizada pelo aluno autenticado.
    @Override
    public List<FichaTreinoResponse> executarPorUsuarioId(
            Long usuarioId
    ) {
        var aluno = alunoRepository
                .buscarPorUsuarioId(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException("Aluno não encontrado")
                );

        return executar(aluno.getId());
    }

    // Consulta utilizada pelo personal autenticado.
    @Override
    public List<FichaTreinoResponse> executarParaPersonal(
            Long usuarioId,
            Long alunoId
    ) {
        var personal = personalRepository
                .buscarPorUsuarioId(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException("Personal não encontrado")
                );

        return fichaTreinoRepository
                .buscarPorAlunoId(alunoId)
                .stream()
                .filter(ficha ->
                        Objects.equals(
                                ficha.getPersonalId(),
                                personal.getId()
                        )
                )
                .map(this::toResponse)
                .toList();
    }

    private FichaTreinoResponse toResponse(FichaTreino ficha) {
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
                                        FichaTreinoResponse.ExercicioResponse
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