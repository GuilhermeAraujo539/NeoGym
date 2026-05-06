package com.neogym.infrastructure.persistence.adapter;

import com.neogym.application.port.out.AlunoRepositoryPort;
import com.neogym.domain.entity.Aluno;
import com.neogym.infrastructure.persistence.entity.UsuarioJpaEntity;
import com.neogym.infrastructure.persistence.mapper.AlunoMapper;
import com.neogym.infrastructure.persistence.repository.AlunoJpaRepository;
import com.neogym.infrastructure.persistence.repository.UsuarioJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AlunoRepositoryAdapter implements AlunoRepositoryPort {

    private final AlunoJpaRepository   alunoJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;
    private final AlunoMapper          mapper;

    @Override
    public Aluno salvar(Aluno aluno) {
        UsuarioJpaEntity usuario = usuarioJpaRepository.findById(aluno.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado ao salvar aluno: " + aluno.getUsuarioId()));
        return mapper.toDomain(alunoJpaRepository.save(mapper.toEntity(aluno, usuario)));
    }

    @Override
    public Optional<Aluno> buscarPorUsuarioId(Long usuarioId) {
        return alunoJpaRepository.findByUsuarioId(usuarioId).map(mapper::toDomain);
    }

    @Override
    public Optional<Aluno> buscarPorId(Long id) {
        return alunoJpaRepository.findById(id).map(mapper::toDomain);
    }
}
