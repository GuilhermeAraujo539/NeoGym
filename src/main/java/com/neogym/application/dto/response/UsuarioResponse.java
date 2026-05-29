package com.neogym.application.dto.response;

import com.neogym.domain.enums.TipoUsuario;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UsuarioResponse {

    private Long          id;
    private String        nome;
    private String        email;
    private TipoUsuario   tipo;
    private boolean       ativo;
    private LocalDateTime criadoEm;
}
