package com.neogym.domain.entity;

import lombok.Getter;

@Getter
public class FichaTreinoExercicio {

    private Long id;
    private String nome;
    private Integer series;
    private Integer repeticoes;
    private Integer ordem;

    public FichaTreinoExercicio(
            Long id,
            String nome,
            Integer series,
            Integer repeticoes,
            Integer ordem
    ) {
        this.id = id;
        this.nome = nome;
        this.series = series;
        this.repeticoes = repeticoes;
        this.ordem = ordem;
    }

    public static FichaTreinoExercicio criar(
            String nome,
            Integer series,
            Integer repeticoes,
            Integer ordem
    ) {
        return new FichaTreinoExercicio(
                null,
                nome,
                series,
                repeticoes,
                ordem
        );
    }
}