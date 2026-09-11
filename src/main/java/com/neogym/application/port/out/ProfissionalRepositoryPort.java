package com.neogym.application.port.out;

import com.neogym.infrastructure.projection.ProfissionalProximoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfissionalRepositoryPort {

    Page<ProfissionalProximoProjection> buscarPersonalsProximos(
            Double latitude,
            Double longitude,
            Double raioKm,
            Pageable pageable
    );
}