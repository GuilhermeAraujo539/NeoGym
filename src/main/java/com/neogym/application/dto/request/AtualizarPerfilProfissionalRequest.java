package com.neogym.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AtualizarPerfilProfissionalRequest {

    @NotBlank(message = "Informe sua especialidade principal")
    @Size(max = 120)
    private String especialidade;

    @Size(max = 3000)
    private String apresentacao;

    @Size(max = 2000)
    private String areasAtuacao;

    @Size(max = 2000)
    private String horariosDisponiveis;
}