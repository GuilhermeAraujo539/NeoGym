package com.neogym.application.usecase;

import com.neogym.application.dto.response.PageResponse;
import com.neogym.application.dto.response.UsuarioResponse;
import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.domain.entity.Usuario;
import com.neogym.domain.enums.TipoUsuario;
import com.neogym.domain.exception.UsuarioNaoEncontradoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public class ListarUsuariosAdminService {

    private final UsuarioRepositoryPort usuarioRepository;

    public ListarUsuariosAdminService(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public PageResponse<UsuarioResponse> listar(TipoUsuario tipo, Boolean ativo,
                                                 Pageable pageable) {
        Page<UsuarioResponse> page = usuarioRepository
                .listarComFiltros(tipo, ativo, pageable)
                .map(this::toResponse);

        return PageResponse.<UsuarioResponse>builder()
                .conteudo(page.getContent())
                .pagina(page.getNumber())
                .tamanhoPagina(page.getSize())
                .totalElementos(page.getTotalElements())
                .totalPaginas(page.getTotalPages())
                .primeira(page.isFirst())
                .ultima(page.isLast())
                .build();
    }

    public UsuarioResponse buscarPorId(Long id) {
        return usuarioRepository.buscarPorId(id)
                .map(this::toResponse)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
    }

    private UsuarioResponse toResponse(Usuario u) {
        return UsuarioResponse.builder()
                .id(u.getId())
                .nome(u.getNome())
                .email(u.getEmail())
                .tipo(u.getTipo())
                .ativo(u.isAtivo())
                .criadoEm(u.getCriadoEm())
                .build();
    }
}
