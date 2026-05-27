package com.neogym.infrastructure.persistence.adapter;

import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.domain.entity.Usuario;
import com.neogym.infrastructure.persistence.mapper.UsuarioMapper;
import com.neogym.infrastructure.persistence.repository.UsuarioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository jpaRepository;
    private final UsuarioMapper        mapper;

    @Override
    public Usuario salvar(Usuario usuario) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(usuario)));
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public boolean existePorEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public org.springframework.data.domain.Page<com.neogym.domain.entity.Usuario> listarComFiltros(
            com.neogym.domain.enums.TipoUsuario tipo,
            Boolean ativo,
            org.springframework.data.domain.Pageable pageable) {
        String tipoStr = tipo != null ? tipo.name() : null;
        return jpaRepository.findComFiltros(tipoStr, ativo, pageable).map(mapper::toDomain);
    }

    @Override
    public void deletar(Long id) {
        jpaRepository.deleteById(id);
    }
}
