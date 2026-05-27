package com.neogym.infrastructure.persistence.adapter;

import com.neogym.application.port.out.AdminRepositoryPort;
import com.neogym.domain.enums.TipoUsuario;
import com.neogym.infrastructure.persistence.repository.PersonalAcademiaJpaRepository;
import com.neogym.infrastructure.persistence.repository.UsuarioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminRepositoryAdapter implements AdminRepositoryPort {

    private final UsuarioJpaRepository         usuarioJpaRepository;
    private final PersonalAcademiaJpaRepository personalAcademiaJpaRepository;

    @Override
    public long contarUsuariosPorTipo(TipoUsuario tipo) {
        return usuarioJpaRepository.countByTipo(tipo.name());
    }

    @Override
    public long contarTotalUsuarios() {
        return usuarioJpaRepository.count();
    }

    @Override
    public long contarUsuariosPorAtivo(boolean ativo) {
        return usuarioJpaRepository.countByAtivo(ativo);
    }

    @Override
    public long contarVinculosPendentes() {
        return personalAcademiaJpaRepository.countByStatus("PENDENTE");
    }
}
