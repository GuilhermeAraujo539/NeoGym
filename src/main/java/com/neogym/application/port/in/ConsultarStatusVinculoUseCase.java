package com.neogym.application.port.in;

import com.neogym.application.dto.response.StatusVinculoResponse;

public interface ConsultarStatusVinculoUseCase {

    StatusVinculoResponse executar(
            String email,
            Long outroUsuarioId
    );
}