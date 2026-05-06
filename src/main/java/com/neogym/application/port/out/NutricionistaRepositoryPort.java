package com.neogym.application.port.out;

import com.neogym.domain.entity.Nutricionista;

import java.util.Optional;

public interface NutricionistaRepositoryPort {
    Nutricionista salvar(Nutricionista nutricionista);
    Optional<Nutricionista> buscarPorUsuarioId(Long usuarioId);
    Optional<Nutricionista> buscarPorId(Long id);
}
