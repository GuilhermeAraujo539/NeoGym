package com.neogym.application.dto.response;

import com.neogym.domain.enums.StatusCredencial;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PerfilProfissionalResponse {

    private Long id;
    private Long usuarioId;

    private String cref;
    private String estadoCref;
    private StatusCredencial statusCref;

    private String especialidade;
    private String apresentacao;
    private String areasAtuacao;
    private String horariosDisponiveis;
}