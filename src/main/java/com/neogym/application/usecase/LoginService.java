package com.neogym.application.usecase;

import com.neogym.application.dto.request.LoginRequest;
import com.neogym.application.dto.response.AuthResponse;
import com.neogym.application.port.in.LoginUseCase;
import com.neogym.application.port.out.AccessTokenPort;
import com.neogym.application.port.out.HashPort;
import com.neogym.application.port.out.PasswordEncoderPort;
import com.neogym.application.port.out.RefreshTokenRepositoryPort;
import com.neogym.application.port.out.RequestMetadataPort;
import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.domain.entity.RefreshToken;
import com.neogym.domain.entity.Usuario;
import com.neogym.domain.exception.AcessoNegadoException;
import com.neogym.domain.exception.CredenciaisInvalidasException;

import java.time.LocalDateTime;


public class LoginService implements LoginUseCase {

    private final UsuarioRepositoryPort      usuarioRepository;
    private final PasswordEncoderPort        passwordEncoder;
    private final AccessTokenPort            accessTokenPort;
    private final HashPort                   hashPort;
    private final RefreshTokenRepositoryPort refreshTokenRepository;
    private final RequestMetadataPort        requestMetadata;
    private final long                       refreshExpirationMs;

    public LoginService(
            UsuarioRepositoryPort      usuarioRepository,
            PasswordEncoderPort        passwordEncoder,
            AccessTokenPort            accessTokenPort,
            HashPort                   hashPort,
            RefreshTokenRepositoryPort refreshTokenRepository,
            RequestMetadataPort        requestMetadata,
            long                       refreshExpirationMs) {
        this.usuarioRepository      = usuarioRepository;
        this.passwordEncoder        = passwordEncoder;
        this.accessTokenPort        = accessTokenPort;
        this.hashPort               = hashPort;
        this.refreshTokenRepository = refreshTokenRepository;
        this.requestMetadata        = requestMetadata;
        this.refreshExpirationMs    = refreshExpirationMs;
    }

    @Override
    public AuthResponse executar(LoginRequest req) {
        String email = req.getEmail().toLowerCase().strip();

        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!passwordEncoder.matches(req.getSenha(), usuario.getSenhaHash())) {
            throw new CredenciaisInvalidasException();
        }

        if (!usuario.isAtivo()) {
            throw new AcessoNegadoException("Conta desativada. Entre em contato com o suporte.");
        }

        String accessToken  = accessTokenPort.gerar(usuario);
        String tokenBruto   = hashPort.gerarTokenAleatorio();
        String tokenHash    = hashPort.sha256(tokenBruto);

        RefreshToken refreshToken = RefreshToken.builder()
                .usuarioId(usuario.getId())
                .tokenHash(tokenHash)
                .expiraEm(LocalDateTime.now().plusSeconds(refreshExpirationMs / 1000))
                .revogado(false)
                .criadoEm(LocalDateTime.now())
                .ipOrigem(requestMetadata.getIpOrigem())
                .userAgent(requestMetadata.getUserAgent())
                .build();

        refreshTokenRepository.salvar(refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(tokenBruto)
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
