package com.neogym.application.dto.request;

import com.neogym.domain.enums.StatusCredencial;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class AvaliarCredencialRequest {

    @NotNull(message = "Status é obrigatório")
    private StatusCredencial status; // APROVADO ou REJEITADO

    @Size(max = 500, message = "Observação deve ter no máximo 500 caracteres")
    private String observacao; // obrigatória se REJEITADO
}
