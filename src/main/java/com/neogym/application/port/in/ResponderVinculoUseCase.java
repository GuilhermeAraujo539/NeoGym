package com.neogym.application.port.in;

import com.neogym.domain.entity.VinculoProfissional;

public interface ResponderVinculoUseCase {

    VinculoProfissional aceitar(
            String email,
            Long vinculoId
    );

    VinculoProfissional recusar(
            String email,
            Long vinculoId
    );
}