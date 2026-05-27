package com.neogym.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class AtualizarUsuarioAdminRequest {

    @Size(max = 120, message = "Nome deve ter no máximo 120 caracteres")
    private String nome;

    @Email(message = "E-mail inválido")
    @Size(max = 120)
    private String email;

    private Boolean ativo; // ativar ou desativar conta
}
