package com.neogym.application.port.out;

import com.neogym.domain.entity.Personal;

import java.util.Optional;

public interface PersonalRepositoryPort {
    Personal salvar(Personal personal);
    Optional<Personal> buscarPorUsuarioId(Long usuarioId);
    Optional<Personal> buscarPorId(Long id);
}
