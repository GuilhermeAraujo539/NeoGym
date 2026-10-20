package com.neogym.application.dto.response;

import lombok.*;

@Getter
@Builder
public class ProfissionalVinculadoResponse {

    private Long vinculoId;
    private Long usuarioId;
    private String nome;
    private String email;
}