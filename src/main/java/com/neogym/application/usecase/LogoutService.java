package com.neogym.application.usecase;

import com.neogym.application.dto.request.RefreshTokenRequest;
import com.neogym.application.port.in.LogoutUseCase;
import com.neogym.application.port.out.HashPort;
import com.neogym.application.port.out.RefreshTokenRepositoryPort;
import com.neogym.domain.entity.RefreshToken;
import com.neogym.domain.exception.RefreshTokenInvalidoException;

public class LogoutService implements LogoutUseCase {

    private final RefreshTokenRepositoryPort refreshTokenRepository;
    private final HashPort                   hashPort;

    public LogoutService(
            RefreshTokenRepositoryPort refreshTokenRepository,
            HashPort hashPort) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.hashPort               = hashPort;
    }

    @Override
    public void executar(RefreshTokenRequest req) {
        String hash = hashPort.sha256(req.getRefreshToken());

        RefreshToken token = refreshTokenRepository.buscarPorHash(hash)
                .orElseThrow(RefreshTokenInvalidoException::new);

        if (token.isRevogado()) {
            return;
        }

        RefreshToken revogado = RefreshToken.builder()
                .id(token.getId())
                .usuarioId(token.getUsuarioId())
                .tokenHash(token.getTokenHash())
                .expiraEm(token.getExpiraEm())
                .revogado(true)
                .criadoEm(token.getCriadoEm())
                .ipOrigem(token.getIpOrigem())
                .userAgent(token.getUserAgent())
                .build();

        refreshTokenRepository.salvar(revogado);
    }
}
