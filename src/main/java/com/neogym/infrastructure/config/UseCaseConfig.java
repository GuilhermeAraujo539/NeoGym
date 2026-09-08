package com.neogym.infrastructure.config;

import com.neogym.application.port.out.*;
import com.neogym.application.usecase.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.neogym.infrastructure.ai.MockGeradorTreinoIAAdapter;

@Configuration
public class UseCaseConfig {

    @Value("${neogym.jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    @Bean
    public CadastrarAlunoService cadastrarAlunoService(
            UsuarioRepositoryPort usuarioRepository,
            AlunoRepositoryPort   alunoRepository,
            PasswordEncoderPort   passwordEncoder) {
        return new CadastrarAlunoService(usuarioRepository, alunoRepository, passwordEncoder);
    }

    @Bean
    public CadastrarPersonalService cadastrarPersonalService(
            UsuarioRepositoryPort  usuarioRepository,
            PersonalRepositoryPort personalRepository,
            PasswordEncoderPort    passwordEncoder) {
        return new CadastrarPersonalService(usuarioRepository, personalRepository, passwordEncoder);
    }

    @Bean
    public CadastrarNutricionistaService cadastrarNutricionistaService(
            UsuarioRepositoryPort       usuarioRepository,
            NutricionistaRepositoryPort nutricionistaRepository,
            PasswordEncoderPort         passwordEncoder) {
        return new CadastrarNutricionistaService(usuarioRepository, nutricionistaRepository, passwordEncoder);
    }

    @Bean
    public LoginService loginService(
            UsuarioRepositoryPort      usuarioRepository,
            PasswordEncoderPort        passwordEncoder,
            AccessTokenPort            accessTokenPort,
            HashPort                   hashPort,
            RefreshTokenRepositoryPort refreshTokenRepository,
            RequestMetadataPort        requestMetadata) {
        return new LoginService(
                usuarioRepository, passwordEncoder, accessTokenPort,
                hashPort, refreshTokenRepository, requestMetadata,
                refreshTokenExpirationMs);
    }

    @Bean
    public RefreshTokenService refreshTokenService(
            RefreshTokenRepositoryPort refreshTokenRepository,
            UsuarioRepositoryPort      usuarioRepository,
            AccessTokenPort            accessTokenPort,
            HashPort                   hashPort,
            RequestMetadataPort        requestMetadata) {
        return new RefreshTokenService(
                refreshTokenRepository, usuarioRepository, accessTokenPort,
                hashPort, requestMetadata, refreshTokenExpirationMs);
    }

    @Bean
    public LogoutService logoutService(
            RefreshTokenRepositoryPort refreshTokenRepository,
            HashPort                   hashPort) {
        return new LogoutService(refreshTokenRepository, hashPort);
    }
    @Bean
    public GerarTreinoIAService gerarTreinoIAService(
            GeradorTreinoIAPort geradorTreinoIAPort,
            PersonalRepositoryPort personalRepository,
            AlunoRepositoryPort alunoRepository,
            PersonalAlunoRepositoryPort personalAlunoRepository) {

        return new GerarTreinoIAService(
                geradorTreinoIAPort,
                personalRepository,
                alunoRepository,
                personalAlunoRepository
        );
    }


}
