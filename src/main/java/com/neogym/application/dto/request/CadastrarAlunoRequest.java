package com.neogym.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Getter
@Builder
@Jacksonized
public class CadastrarAlunoRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 120, message = "Nome deve ter no máximo 120 caracteres")
    private String nome;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Size(max = 120)
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, max = 100, message = "Senha deve ter entre 8 e 100 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
        message = "Senha deve conter pelo menos uma letra minúscula, uma maiúscula e um número"
    )
    private String senha;

    private Long academiaId;

    @DecimalMin(value = "0.1", inclusive = true, message = "Peso deve ser positivo")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal peso;

    @DecimalMin(value = "0.1", inclusive = true, message = "Altura deve ser positiva")
    @Digits(integer = 2, fraction = 2)
    private BigDecimal altura;

    @Min(value = 500, message = "Meta de água mínima é 500ml")
    @Max(value = 10000, message = "Meta de água máxima é 10000ml")
    private Integer metaAguaMl;
}
