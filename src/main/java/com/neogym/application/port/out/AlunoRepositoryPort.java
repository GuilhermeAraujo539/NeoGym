package com.neogym.application.port.out;

import com.neogym.domain.entity.Aluno;

import java.util.Optional;

public interface AlunoRepositoryPort {
    Aluno salvar(Aluno aluno);
    Optional<Aluno> buscarPorUsuarioId(Long usuarioId);
    Optional<Aluno> buscarPorId(Long id);
}
