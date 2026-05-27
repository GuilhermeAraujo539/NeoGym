package com.neogym.application.port.out;

import com.neogym.domain.entity.Usuario;

import java.util.Optional;

public interface UsuarioRepositoryPort {
    Usuario salvar(Usuario usuario);
    Optional<Usuario> buscarPorEmail(String email);
    Optional<Usuario> buscarPorId(Long id);
    boolean existePorEmail(String email);

    org.springframework.data.domain.Page<com.neogym.domain.entity.Usuario> listarComFiltros(
            com.neogym.domain.enums.TipoUsuario tipo,
            Boolean ativo,
            org.springframework.data.domain.Pageable pageable);

    void deletar(Long id);
}
