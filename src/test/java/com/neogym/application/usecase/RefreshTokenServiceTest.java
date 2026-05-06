package com.neogym.application.usecase;

import com.neogym.application.dto.request.RefreshTokenRequest;
import com.neogym.application.dto.response.AuthResponse;
import com.neogym.application.port.out.*;
import com.neogym.domain.entity.RefreshToken;
import com.neogym.domain.entity.Usuario;
import com.neogym.domain.enums.TipoUsuario;
import com.neogym.domain.exception.RefreshTokenInvalidoException;
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
@DisplayName("RefreshTokenService")
class RefreshTokenServiceTest {

    @Mock private RefreshTokenRepositoryPort refreshTokenRepository;
    @Mock private UsuarioRepositoryPort      usuarioRepository;
    @Mock private AccessTokenPort            accessTokenPort;
    @Mock private HashPort                   hashPort;
    @Mock private RequestMetadataPort        requestMetadata;

    private RefreshTokenService service;
    private RefreshToken        tokenValido;
    private Usuario             usuario;

    @BeforeEach
    void setUp() {
        service = new RefreshTokenService(
                refreshTokenRepository, usuarioRepository,
                accessTokenPort, hashPort, requestMetadata, 604800000L);

        tokenValido = RefreshToken.builder()
                .id(1L).usuarioId(1L).tokenHash("hashAntigo")
                .expiraEm(LocalDateTime.now().plusDays(7))
                .revogado(false).criadoEm(LocalDateTime.now()).build();

        usuario = Usuario.builder()
                .id(1L).nome("João").email("joao@email.com")
                .senhaHash("hash").tipo(TipoUsuario.ALUNO)
                .ativo(true).criadoEm(LocalDateTime.now()).build();
    }

    @Test
    @DisplayName("deve rotacionar: revogar token antigo e emitir novo par")
    void deveRotacionarToken() {
        when(hashPort.sha256("tokenBruto")).thenReturn("hashAntigo");
        when(refreshTokenRepository.buscarPorHash("hashAntigo")).thenReturn(Optional.of(tokenValido));
        when(usuarioRepository.buscarPorId(1L)).thenReturn(Optional.of(usuario));
        when(accessTokenPort.gerar(usuario)).thenReturn("novoJwt");
        when(accessTokenPort.expiracaoMs()).thenReturn(900000L);
        when(hashPort.gerarTokenAleatorio()).thenReturn("novoTokenBruto");
        when(hashPort.sha256("novoTokenBruto")).thenReturn("novoHash");
        when(refreshTokenRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(requestMetadata.getIpOrigem()).thenReturn("127.0.0.1");
        when(requestMetadata.getUserAgent()).thenReturn("Agent");

        AuthResponse resp = service.executar(
                RefreshTokenRequest.builder().refreshToken("tokenBruto").build());

        assertThat(resp.getAccessToken()).isEqualTo("novoJwt");
        assertThat(resp.getRefreshToken()).isEqualTo("novoTokenBruto");

        // token antigo deve ter sido revogado
        verify(refreshTokenRepository).salvar(argThat(rt -> rt.getId().equals(1L) && rt.isRevogado()));
        // novo token deve ter sido salvo
        verify(refreshTokenRepository).salvar(argThat(rt -> rt.getId() == null && !rt.isRevogado()));
    }

    @Test
    @DisplayName("deve revogar TODOS os tokens ao detectar replay attack")
    void deveRevogarTudoEmReplayAttack() {
        RefreshToken jaRevogado = RefreshToken.builder()
                .id(1L).usuarioId(1L).tokenHash("hash")
                .expiraEm(LocalDateTime.now().plusDays(7))
                .revogado(true).criadoEm(LocalDateTime.now()).build();

        when(hashPort.sha256("tokenBruto")).thenReturn("hash");
        when(refreshTokenRepository.buscarPorHash("hash")).thenReturn(Optional.of(jaRevogado));

        assertThatThrownBy(() -> service.executar(
                RefreshTokenRequest.builder().refreshToken("tokenBruto").build()))
                .isInstanceOf(RefreshTokenInvalidoException.class);

        verify(refreshTokenRepository).revogarTodosPorUsuarioId(1L);
    }

    @Test
    @DisplayName("deve lançar exceção quando token não encontrado")
    void deveLancarExcecaoTokenInexistente() {
        when(hashPort.sha256(any())).thenReturn("hashInexistente");
        when(refreshTokenRepository.buscarPorHash("hashInexistente")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.executar(
                RefreshTokenRequest.builder().refreshToken("qualquer").build()))
                .isInstanceOf(RefreshTokenInvalidoException.class);
    }

    @Test
    @DisplayName("deve lançar exceção quando token expirado")
    void deveLancarExcecaoTokenExpirado() {
        RefreshToken expirado = RefreshToken.builder()
                .id(2L).usuarioId(1L).tokenHash("hashExp")
                .expiraEm(LocalDateTime.now().minusHours(1))
                .revogado(false).criadoEm(LocalDateTime.now().minusDays(8)).build();

        when(hashPort.sha256("tokenExp")).thenReturn("hashExp");
        when(refreshTokenRepository.buscarPorHash("hashExp")).thenReturn(Optional.of(expirado));

        assertThatThrownBy(() -> service.executar(
                RefreshTokenRequest.builder().refreshToken("tokenExp").build()))
                .isInstanceOf(RefreshTokenInvalidoException.class);
    }
}
