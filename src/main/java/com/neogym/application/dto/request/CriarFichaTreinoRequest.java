package com.neogym.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CriarFichaTreinoRequest {

    @NotNull
    private Long alunoId;

    @NotBlank
    private String nome;

    private String objetivo;

    private Integer duracao;

    @Valid
    @NotEmpty
    private List<ExercicioFichaRequest> exercicios;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ExercicioFichaRequest {

        @NotBlank
        private String nome;

        @NotNull
        private Integer series;

        @NotNull
        private Integer repeticoes;
    }
}