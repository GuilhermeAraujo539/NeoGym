package com.neogym.domain.entity;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class FichaTreino {

    private Long id;
    private Long alunoId;
    private Long personalId;
    private String nome;
    private String objetivo;
    private Integer duracao;
    private LocalDateTime criadaEm;

    private List<FichaTreinoExercicio> exercicios =
            new ArrayList<>();

    public FichaTreino(
            Long id,
            Long alunoId,
            Long personalId,
            String nome,
            String objetivo,
            Integer duracao,
            LocalDateTime criadaEm,
            List<FichaTreinoExercicio> exercicios
    ) {
        this.id = id;
        this.alunoId = alunoId;
        this.personalId = personalId;
        this.nome = nome;
        this.objetivo = objetivo;
        this.duracao = duracao;
        this.criadaEm = criadaEm;

        if (exercicios != null) {
            this.exercicios = exercicios;
        }
    }

    public static FichaTreino criar(
            Long alunoId,
            Long personalId,
            String nome,
            String objetivo,
            Integer duracao,
            List<FichaTreinoExercicio> exercicios
    ) {
        return new FichaTreino(
                null,
                alunoId,
                personalId,
                nome,
                objetivo,
                duracao,
                LocalDateTime.now(),
                exercicios
        );
    }
}