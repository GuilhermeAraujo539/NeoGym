package com.neogym.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FichaTreinoResponse {

    private Long id;
    private Long alunoId;
    private Long personalId;
    private String nome;
    private String objetivo;
    private Integer duracao;
    private List<ExercicioResponse> exercicios;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExercicioResponse {

        private Long id;
        private String nome;
        private Integer series;
        private Integer repeticoes;
        private Integer ordem;
    }
}