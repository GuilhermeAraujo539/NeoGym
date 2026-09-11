package com.neogym.application.port.in;

import com.neogym.application.dto.request.SolicitarVinculoRequest;
import com.neogym.domain.entity.VinculoProfissional;

public interface SolicitarVinculoUseCase {

    VinculoProfissional executar(
            String email,
            SolicitarVinculoRequest request
    );
}