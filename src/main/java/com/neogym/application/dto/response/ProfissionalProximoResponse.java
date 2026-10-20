package com.neogym.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfissionalProximoResponse {

    private Long id;
    private Long usuarioId;
    private String nome;
    private String email;
    private String tipo;
    private Double distanciaKm;
    private String especialidade;
    private String apresentacao;
    private String areasAtuacao;
    private String horariosDisponiveis;
}