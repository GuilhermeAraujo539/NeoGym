package com.neogym.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class AlunoResponse {
    private Long          id;
    private Long          usuarioId;
    private String        nome;
    private String        email;
    private Long          academiaId;
    private BigDecimal    peso;
    private BigDecimal    altura;
    private Integer       metaAguaMl;
    private boolean       ativo;
    private LocalDateTime criadoEm;
}
