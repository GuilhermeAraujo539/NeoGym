package com.neogym.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class TreinoIAResponse {

    private String nome;
    private String objetivo;
    private Integer duracao;
    private List<ExercicioIAResponse> exercicios;
}