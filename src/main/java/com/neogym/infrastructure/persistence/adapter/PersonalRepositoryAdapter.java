package com.neogym.infrastructure.persistence.adapter;

import com.neogym.application.port.out.PersonalRepositoryPort;
import com.neogym.domain.entity.Personal;
import com.neogym.infrastructure.persistence.entity.UsuarioJpaEntity;
import com.neogym.infrastructure.persistence.mapper.PersonalMapper;
import com.neogym.infrastructure.persistence.repository.PersonalJpaRepository;
import com.neogym.infrastructure.persistence.repository.UsuarioJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PersonalRepositoryAdapter implements PersonalRepositoryPort {

    private final PersonalJpaRepository personalJpaRepository;
    private final UsuarioJpaRepository  usuarioJpaRepository;
    private final PersonalMapper        mapper;

    @Override
    public Personal salvar(Personal personal) {
        UsuarioJpaEntity usuario = usuarioJpaRepository.findById(personal.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado ao salvar personal: " + personal.getUsuarioId()));
        return mapper.toDomain(personalJpaRepository.save(mapper.toEntity(personal, usuario)));
    }

    @Override
    public Optional<Personal> buscarPorUsuarioId(Long usuarioId) {
        return personalJpaRepository.findByUsuarioId(usuarioId).map(mapper::toDomain);
    }

    @Override
    public Optional<Personal> buscarPorId(Long id) {
        return personalJpaRepository.findById(id).map(mapper::toDomain);
    }
}
