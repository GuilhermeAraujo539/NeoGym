package com.neogym.application.usecase;

import com.neogym.application.dto.request.AtualizarUsuarioAdminRequest;
import com.neogym.application.dto.response.UsuarioResponse;
import com.neogym.application.port.out.RefreshTokenRepositoryPort;
import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.domain.entity.Usuario;
import com.neogym.domain.exception.OperacaoNaoPermitidaException;
import com.neogym.domain.exception.UsuarioNaoEncontradoException;

public class GerenciarUsuarioAdminService {

    private final UsuarioRepositoryPort      usuarioRepository;
    private final RefreshTokenRepositoryPort refreshTokenRepository;

    public GerenciarUsuarioAdminService(
            UsuarioRepositoryPort      usuarioRepository,
            RefreshTokenRepositoryPort refreshTokenRepository) {
        this.usuarioRepository      = usuarioRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public UsuarioResponse atualizarUsuario(Long usuarioId, Long adminId,
                                             AtualizarUsuarioAdminRequest req) {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));

        if (usuario.isAdmin() && !usuarioId.equals(adminId)) {
            throw new OperacaoNaoPermitidaException(
                    "Não é permitido alterar contas de outros administradores.");
        }

        if (req.getAtivo() != null && !req.getAtivo()) {
            refreshTokenRepository.revogarTodosPorUsuarioId(usuarioId);
        }

        Usuario atualizado = Usuario.builder()
                .id(usuario.getId())
                .nome(req.getNome() != null ? req.getNome().strip() : usuario.getNome())
                .email(req.getEmail() != null
                        ? req.getEmail().toLowerCase().strip() : usuario.getEmail())
                .senhaHash(usuario.getSenhaHash())
                .tipo(usuario.getTipo())
                .ativo(req.getAtivo() != null ? req.getAtivo() : usuario.isAtivo())
                .criadoEm(usuario.getCriadoEm())
                .build();

        Usuario salvo = usuarioRepository.salvar(atualizado);
        return toResponse(salvo);
    }

    public void deletarUsuario(Long usuarioId, Long adminId) {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));

        if (usuario.isAdmin()) {
            throw new OperacaoNaoPermitidaException(
                    "Não é permitido deletar contas de administradores.");
        }

        if (usuarioId.equals(adminId)) {
            throw new OperacaoNaoPermitidaException(
                    "Você não pode deletar sua própria conta.");
        }

        refreshTokenRepository.revogarTodosPorUsuarioId(usuarioId);
        usuarioRepository.deletar(usuarioId);
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
