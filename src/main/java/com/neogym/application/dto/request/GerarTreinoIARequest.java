package com.neogym.application.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GerarTreinoIARequest {

    @NotNull
    private Long alunoId;

    @NotBlank
    private String objetivo;

    @NotBlank
    private String nivel;

    @NotNull
    @Min(1)
    @Max(7)
    private Integer diasSemana;

    @NotNull
    @Min(15)
    @Max(240)
    private Integer duracaoMinutos;

    private String observacoes;
}