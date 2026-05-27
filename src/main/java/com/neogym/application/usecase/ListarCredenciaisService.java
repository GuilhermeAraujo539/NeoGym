package com.neogym.application.usecase;

import com.neogym.application.dto.response.CredencialResponse;
import com.neogym.application.dto.response.PageResponse;
import com.neogym.application.port.out.CredencialRepositoryPort;
import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.domain.enums.StatusCredencial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarCredenciaisService {

    private final CredencialRepositoryPort credencialRepository;
    private final UsuarioRepositoryPort    usuarioRepository;

    public ListarCredenciaisService(CredencialRepositoryPort credencialRepository,
                                    UsuarioRepositoryPort    usuarioRepository) {
        this.credencialRepository = credencialRepository;
        this.usuarioRepository    = usuarioRepository;
    }

    /** Admin: lista todas as credenciais ou filtra por status. */
    public PageResponse<CredencialResponse> listarPorStatus(StatusCredencial status,
                                                            Pageable pageable) {
        Page<CredencialResponse> page = (status != null
                ? credencialRepository.listarPorStatus(status, pageable)
                : credencialRepository.listarTodas(pageable))
                .map(c -> {
                    var usuario = usuarioRepository.buscarPorId(c.getUsuarioId()).orElse(null);
                    return EnviarCredencialService.toResponse(c, usuario, null, null);
                });

        return toPageResponse(page);
    }

    /** Profissional: lista apenas suas próprias credenciais. */
    public PageResponse<CredencialResponse> listarPorUsuario(Long usuarioId,
                                                             Pageable pageable) {
        Page<CredencialResponse> page = credencialRepository
                .listarPorUsuarioId(usuarioId, pageable)
                .map(c -> {
                    var usuario = usuarioRepository.buscarPorId(c.getUsuarioId()).orElse(null);
                    return EnviarCredencialService.toResponse(c, usuario, null, null);
                });

        return toPageResponse(page);
    }

    private PageResponse<CredencialResponse> toPageResponse(Page<CredencialResponse> page) {
        return PageResponse.<CredencialResponse>builder()
                .conteudo(page.getContent())
                .pagina(page.getNumber())
                .tamanhoPagina(page.getSize())
                .totalElementos(page.getTotalElements())
                .totalPaginas(page.getTotalPages())
                .primeira(page.isFirst())
                .ultima(page.isLast())
                .build();
    }
}