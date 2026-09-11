package com.neogym.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlunoVinculadoResponse {

    private Long vinculoId;
    private Long alunoId;
    private Long usuarioId;
    private String nome;
    private String email;
}