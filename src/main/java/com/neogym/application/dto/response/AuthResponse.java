package com.neogym.application.dto.response;

import com.neogym.domain.enums.TipoUsuario;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {

    private String      accessToken;
    private String      refreshToken;
    private String      tokenType;
    private long        accessTokenExpiresIn;
    private long        refreshTokenExpiresIn;
    private Long        usuarioId;
    private String      nome;
    private String      email;
    private TipoUsuario tipo;
}
