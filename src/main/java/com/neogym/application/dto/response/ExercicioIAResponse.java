package com.neogym.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ExercicioIAResponse {

    private String nome;
    private Integer series;
    private Integer repeticoes;
}