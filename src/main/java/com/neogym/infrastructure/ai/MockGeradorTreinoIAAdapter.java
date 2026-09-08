package com.neogym.infrastructure.ai;

import com.neogym.application.dto.request.GerarTreinoIARequest;
import com.neogym.application.dto.response.ExercicioIAResponse;
import com.neogym.application.dto.response.TreinoIAResponse;
import com.neogym.application.port.out.GeradorTreinoIAPort;

import java.util.List;

public class MockGeradorTreinoIAAdapter implements GeradorTreinoIAPort {

    @Override
    public TreinoIAResponse gerar(GerarTreinoIARequest request) {

        List<ExercicioIAResponse> exercicios = List.of(
                ExercicioIAResponse.builder()
                        .nome("Supino reto")
                        .series(4)
                        .repeticoes(10)
                        .build(),

                ExercicioIAResponse.builder()
                        .nome("Remada baixa")
                        .series(4)
                        .repeticoes(10)
                        .build(),

                ExercicioIAResponse.builder()
                        .nome("Desenvolvimento com halteres")
                        .series(3)
                        .repeticoes(12)
                        .build(),

                ExercicioIAResponse.builder()
                        .nome("Rosca direta")
                        .series(3)
                        .repeticoes(12)
                        .build(),

                ExercicioIAResponse.builder()
                        .nome("Tríceps pulley")
                        .series(3)
                        .repeticoes(12)
                        .build()
        );

        return TreinoIAResponse.builder()
                .nome("Treino gerado por IA - " + request.getObjetivo())
                .objetivo(request.getObjetivo())
                .duracao(request.getDuracaoMinutos())
                .exercicios(exercicios)
                .build();
    }
}