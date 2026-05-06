package com.neogym.application.usecase;

import com.neogym.application.dto.request.RefreshTokenRequest;
import com.neogym.application.dto.response.AuthResponse;
import com.neogym.application.port.in.RefreshTokenUseCase;
import com.neogym.application.port.out.AccessTokenPort;
import com.neogym.application.port.out.HashPort;
import com.neogym.application.port.out.RefreshTokenRepositoryPort;
import com.neogym.application.port.out.RequestMetadataPort;
import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.domain.entity.RefreshToken;
import com.neogym.domain.entity.Usuario;
import com.neogym.domain.exception.AcessoNegadoException;
import com.neogym.domain.exception.RefreshTokenInvalidoException;
import com.neogym.domain.exception.UsuarioNaoEncontradoException;

import java.time.LocalDateTime;


public class RefreshTokenService implements RefreshTokenUseCase {

    private final RefreshTokenRepositoryPort refreshTokenRepository;
    private final UsuarioRepositoryPort      usuarioRepository;
    private final AccessTokenPort            accessTokenPort;
    private final HashPort                   hashPort;
    private final RequestMetadataPort        requestMetadata;
    private final long                       refreshExpirationMs;

    public RefreshTokenService(
            RefreshTokenRepositoryPort refreshTokenRepository,
            UsuarioRepositoryPort      usuarioRepository,
            AccessTokenPort            accessTokenPort,
            HashPort                   hashPort,
            RequestMetadataPort        requestMetadata,
            long                       refreshExpirationMs) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.usuarioRepository      = usuarioRepository;
        this.accessTokenPort        = accessTokenPort;
        this.hashPort               = hashPort;
        this.requestMetadata        = requestMetadata;
        this.refreshExpirationMs    = refreshExpirationMs;
    }

    @Override
    public AuthResponse executar(RefreshTokenRequest req) {
        String hashRecebido = hashPort.sha256(req.getRefreshToken());

        RefreshToken tokenAntigo = refreshTokenRepository.buscarPorHash(hashRecebido)
                .orElseThrow(RefreshTokenInvalidoException::new);

        if (tokenAntigo.isRevogado()) {
            refreshTokenRepository.revogarTodosPorUsuarioId(tokenAntigo.getUsuarioId());
            throw new RefreshTokenInvalidoException();
        }

        if (tokenAntigo.estaExpirado()) {
            throw new RefreshTokenInvalidoException();
        }

        RefreshToken revogado = RefreshToken.builder()
                .id(tokenAntigo.getId())
                .usuarioId(tokenAntigo.getUsuarioId())
                .tokenHash(tokenAntigo.getTokenHash())
                .expiraEm(tokenAntigo.getExpiraEm())
                .revogado(true)
                .criadoEm(tokenAntigo.getCriadoEm())
                .ipOrigem(tokenAntigo.getIpOrigem())
                .userAgent(tokenAntigo.getUserAgent())
                .build();
        refreshTokenRepository.salvar(revogado);

        Usuario usuario = usuarioRepository.buscarPorId(tokenAntigo.getUsuarioId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException(tokenAntigo.getUsuarioId()));

        if (!usuario.isAtivo()) {
            throw new AcessoNegadoException("Conta desativada.");
        }

        String novoAccessToken = accessTokenPort.gerar(usuario);
        String novoTokenBruto  = hashPort.gerarTokenAleatorio();
        String novoTokenHash   = hashPort.sha256(novoTokenBruto);

        RefreshToken novoToken = RefreshToken.builder()
                .usuarioId(usuario.getId())
                .tokenHash(novoTokenHash)
                .expiraEm(LocalDateTime.now().plusSeconds(refreshExpirationMs / 1000))
                .revogado(false)
                .criadoEm(LocalDateTime.now())
                .ipOrigem(requestMetadata.getIpOrigem())
                .userAgent(requestMetadata.getUserAgent())
                .build();
        refreshTokenRepository.salvar(novoToken);

        return AuthResponse.builder()
                .accessToken(novoAccessToken)
                .refreshToken(novoTokenBruto)
                .tokenType("Bearer")
                .accessTokenExpiresIn(accessTokenPort.expiracaoMs())
                .refreshTokenExpiresIn(refreshExpirationMs)
                .usuarioId(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .tipo(usuario.getTipo())
                .build();
    }
}
