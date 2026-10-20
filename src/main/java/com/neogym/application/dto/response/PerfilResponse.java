package com.neogym.application.dto.response;

import com.neogym.domain.enums.TipoUsuario;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PerfilResponse {

    private Long id;
    private String nome;
    private String email;
    private TipoUsuario tipo;
}