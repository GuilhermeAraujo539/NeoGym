package com.neogym.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class CadastrarPersonalRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 120)
    private String nome;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Size(max = 120)
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, max = 100)
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
        message = "Senha deve conter pelo menos uma letra minúscula, uma maiúscula e um número"
    )
    private String senha;

    @NotBlank(message = "CREF é obrigatório")
    @Size(max = 20, message = "CREF deve ter no máximo 20 caracteres")
    private String cref;

    @NotBlank(message = "Estado do CREF é obrigatório")
    @Size(min = 2, max = 2, message = "Estado deve ser a UF com 2 letras (ex: SP)")
    private String estadoCref;
}
