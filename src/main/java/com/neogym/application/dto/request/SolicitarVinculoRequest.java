package com.neogym.application.dto.request;

import com.neogym.domain.enums.TipoUsuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitarVinculoRequest {

    private Long profissionalUsuarioId;

    private TipoUsuario tipoProfissional;
}