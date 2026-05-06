package com.neogym.infrastructure.persistence.adapter;

import com.neogym.application.port.out.NutricionistaRepositoryPort;
import com.neogym.domain.entity.Nutricionista;
import com.neogym.infrastructure.persistence.entity.UsuarioJpaEntity;
import com.neogym.infrastructure.persistence.mapper.NutricionistaMapper;
import com.neogym.infrastructure.persistence.repository.NutricionistaJpaRepository;
import com.neogym.infrastructure.persistence.repository.UsuarioJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NutricionistaRepositoryAdapter implements NutricionistaRepositoryPort {

    private final NutricionistaJpaRepository nutricionistaJpaRepository;
    private final UsuarioJpaRepository       usuarioJpaRepository;
    private final NutricionistaMapper        mapper;

    @Override
    public Nutricionista salvar(Nutricionista nutricionista) {
        UsuarioJpaEntity usuario = usuarioJpaRepository.findById(nutricionista.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado ao salvar nutricionista: " + nutricionista.getUsuarioId()));
        return mapper.toDomain(nutricionistaJpaRepository.save(mapper.toEntity(nutricionista, usuario)));
    }

    @Override
    public Optional<Nutricionista> buscarPorUsuarioId(Long usuarioId) {
        return nutricionistaJpaRepository.findByUsuarioId(usuarioId).map(mapper::toDomain);
    }

    @Override
    public Optional<Nutricionista> buscarPorId(Long id) {
        return nutricionistaJpaRepository.findById(id).map(mapper::toDomain);
    }
}
