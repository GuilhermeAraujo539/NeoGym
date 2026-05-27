package com.neogym.infrastructure.persistence.adapter;

import com.neogym.application.port.out.CredencialRepositoryPort;
import com.neogym.domain.entity.Credencial;
import com.neogym.domain.enums.StatusCredencial;
import com.neogym.infrastructure.persistence.entity.UsuarioJpaEntity;
import com.neogym.infrastructure.persistence.mapper.CredencialMapper;
import com.neogym.infrastructure.persistence.repository.CredencialJpaRepository;
import com.neogym.infrastructure.persistence.repository.UsuarioJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CredencialRepositoryAdapter implements CredencialRepositoryPort {

    private final CredencialJpaRepository credencialJpaRepository;
    private final UsuarioJpaRepository    usuarioJpaRepository;
    private final CredencialMapper        mapper;

    @Override
    public Credencial salvar(Credencial credencial) {
        UsuarioJpaEntity usuario = usuarioJpaRepository.findById(credencial.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Usuário não encontrado: " + credencial.getUsuarioId()));

        UsuarioJpaEntity avaliador = null;
        if (credencial.getAvaliadoPorId() != null) {
            avaliador = usuarioJpaRepository.findById(credencial.getAvaliadoPorId())
                    .orElse(null);
        }

        return mapper.toDomain(
                credencialJpaRepository.save(mapper.toEntity(credencial, usuario, avaliador)));
    }

    @Override
    public Optional<Credencial> buscarPorId(Long id) {
        return credencialJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Credencial> buscarPorUsuarioId(Long usuarioId) {
        return credencialJpaRepository.findTopByUsuarioId(usuarioId).map(mapper::toDomain);
    }

    @Override
    public Page<Credencial> listarPorStatus(StatusCredencial status, Pageable pageable) {
        return credencialJpaRepository.findByStatus(status.name(), pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Credencial> listarTodas(Pageable pageable) {
        return credencialJpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public long contarPorStatus(StatusCredencial status) {
        return credencialJpaRepository.countByStatus(status.name());
    }

    @Override
    public org.springframework.data.domain.Page<com.neogym.domain.entity.Credencial> listarPorUsuarioId(
            Long usuarioId,
            org.springframework.data.domain.Pageable pageable) {
        return credencialJpaRepository
                .findByUsuarioIdPaginado(usuarioId, pageable)
                .map(mapper::toDomain);
    }
}
