package com.neogym.application.usecase;

import com.neogym.application.dto.request.LoginRequest;
import com.neogym.application.dto.response.AuthResponse;
import com.neogym.application.port.out.*;
import com.neogym.domain.entity.RefreshToken;
import com.neogym.domain.entity.Usuario;
import com.neogym.domain.enums.TipoUsuario;
import com.neogym.domain.exception.AcessoNegadoException;
import com.neogym.domain.exception.CredenciaisInvalidasException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginService")
class LoginServiceTest {

    @Mock private UsuarioRepositoryPort      usuarioRepository;
    @Mock private PasswordEncoderPort        passwordEncoder;
    @Mock private AccessTokenPort            accessTokenPort;
    @Mock private HashPort                   hashPort;
    @Mock private RefreshTokenRepositoryPort refreshTokenRepository;
    @Mock private RequestMetadataPort        requestMetadata;

    private LoginService service;
    private Usuario usuarioAtivo;

    @BeforeEach
    void setUp() {
        service = new LoginService(
                usuarioRepository, passwordEncoder, accessTokenPort,
                hashPort, refreshTokenRepository, requestMetadata,
                604800000L);

        usuarioAtivo = Usuario.builder()
                .id(1L).nome("João").email("joao@email.com")
                .senhaHash("$2a$12$hash").tipo(TipoUsuario.ALUNO)
                .ativo(true).criadoEm(LocalDateTime.now()).build();
    }

    @Test
    @DisplayName("deve retornar access e refresh token no login bem-sucedido")
    void deveRetornarTokensNoLogin() {
        when(usuarioRepository.buscarPorEmail("joao@email.com")).thenReturn(Optional.of(usuarioAtivo));
        when(passwordEncoder.matches("Senha123", "$2a$12$hash")).thenReturn(true);
        when(accessTokenPort.gerar(usuarioAtivo)).thenReturn("jwt-access-token");
        when(accessTokenPort.expiracaoMs()).thenReturn(900000L);
        when(hashPort.gerarTokenAleatorio()).thenReturn("token-bruto");
        when(hashPort.sha256("token-bruto")).thenReturn("sha256hash");
        when(refreshTokenRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(requestMetadata.getIpOrigem()).thenReturn("127.0.0.1");
        when(requestMetadata.getUserAgent()).thenReturn("TestAgent");

        AuthResponse response = service.executar(
                LoginRequest.builder().email("joao@email.com").senha("Senha123").build());

        assertThat(response.getAccessToken()).isEqualTo("jwt-access-token");
        assertThat(response.getRefreshToken()).isEqualTo("token-bruto");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getUsuarioId()).isEqualTo(1L);
        assertThat(response.getTipo()).isEqualTo(TipoUsuario.ALUNO);
    }

    @Test
    @DisplayName("deve lançar CredenciaisInvalidasException quando email não encontrado")
    void deveLancarExcecaoEmailNaoEncontrado() {
        when(usuarioRepository.buscarPorEmail(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.executar(
                LoginRequest.builder().email("x@x.com").senha("abc").build()))
                .isInstanceOf(CredenciaisInvalidasException.class);
    }

    @Test
    @DisplayName("deve lançar CredenciaisInvalidasException quando senha errada")
    void deveLancarExcecaoSenhaErrada() {
        when(usuarioRepository.buscarPorEmail("joao@email.com")).thenReturn(Optional.of(usuarioAtivo));
        when(passwordEncoder.matches("errada", "$2a$12$hash")).thenReturn(false);

        assertThatThrownBy(() -> service.executar(
                LoginRequest.builder().email("joao@email.com").senha("errada").build()))
                .isInstanceOf(CredenciaisInvalidasException.class);

        verify(accessTokenPort, never()).gerar(any());
    }

    @Test
    @DisplayName("deve lançar AcessoNegadoException quando conta inativa")
    void deveLancarExcecaoContaInativa() {
        Usuario inativo = Usuario.builder()
                .id(2L).nome("Maria").email("maria@email.com")
                .senhaHash("hash").tipo(TipoUsuario.ALUNO)
                .ativo(false).criadoEm(LocalDateTime.now()).build();

        when(usuarioRepository.buscarPorEmail("maria@email.com")).thenReturn(Optional.of(inativo));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        assertThatThrownBy(() -> service.executar(
                LoginRequest.builder().email("maria@email.com").senha("Senha123").build()))
                .isInstanceOf(AcessoNegadoException.class);
    }

    @Test
    @DisplayName("deve armazenar hash SHA-256, nunca o token bruto")
    void deveArmazenarHashNoRefreshToken() {
        when(usuarioRepository.buscarPorEmail(any())).thenReturn(Optional.of(usuarioAtivo));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(accessTokenPort.gerar(any())).thenReturn("jwt");
        when(accessTokenPort.expiracaoMs()).thenReturn(900000L);
        when(hashPort.gerarTokenAleatorio()).thenReturn("tokenBruto");
        when(hashPort.sha256("tokenBruto")).thenReturn("hashSHA256");
        when(refreshTokenRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(requestMetadata.getIpOrigem()).thenReturn("127.0.0.1");
        when(requestMetadata.getUserAgent()).thenReturn("Agent");

        service.executar(LoginRequest.builder().email("joao@email.com").senha("Senha123").build());

        verify(refreshTokenRepository).salvar(argThat(rt ->
                "hashSHA256".equals(rt.getTokenHash()) && !rt.getTokenHash().equals("tokenBruto")));
    }
}
