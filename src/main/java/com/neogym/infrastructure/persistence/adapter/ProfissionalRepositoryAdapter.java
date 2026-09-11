package com.neogym.infrastructure.persistence.adapter;

import com.neogym.infrastructure.projection.ProfissionalProximoProjection;
import com.neogym.application.port.out.ProfissionalRepositoryPort;
import com.neogym.infrastructure.persistence.repository.ProfissionalJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfissionalRepositoryAdapter
        implements ProfissionalRepositoryPort {

    private final ProfissionalJpaRepository repository;

    @Override
    public Page<ProfissionalProximoProjection> buscarPersonalsProximos(
            Double latitude,
            Double longitude,
            Double raioKm,
            Pageable pageable
    ) {
        return repository.buscarPersonalsProximos(
                latitude,
                longitude,
                raioKm,
                pageable
        );
    }
}