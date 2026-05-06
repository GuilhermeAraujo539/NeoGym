package com.neogym.application.usecase;

import com.neogym.application.dto.request.CadastrarAlunoRequest;
import com.neogym.application.dto.response.AlunoResponse;
import com.neogym.application.port.out.AlunoRepositoryPort;
import com.neogym.application.port.out.PasswordEncoderPort;
import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.domain.entity.Aluno;
import com.neogym.domain.entity.Usuario;
import com.neogym.domain.enums.TipoUsuario;
import com.neogym.domain.exception.UsuarioJaExisteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CadastrarAlunoService")
class CadastrarAlunoServiceTest {

    @Mock private UsuarioRepositoryPort usuarioRepository;
    @Mock private AlunoRepositoryPort   alunoRepository;
    @Mock private PasswordEncoderPort   passwordEncoder;

    private CadastrarAlunoService service;

    @BeforeEach
    void setUp() {
        service = new CadastrarAlunoService(usuarioRepository, alunoRepository, passwordEncoder);
    }

    @Test
    @DisplayName("deve cadastrar aluno com sucesso quando dados válidos")
    void deveCadastrarAlunoComSucesso() {
        when(usuarioRepository.existePorEmail("joao@email.com")).thenReturn(false);
        when(passwordEncoder.encode("Senha123")).thenReturn("$2a$12$hash");

        Usuario usuarioSalvo = Usuario.builder()
                .id(1L).nome("João Silva").email("joao@email.com")
                .senhaHash("$2a$12$hash").tipo(TipoUsuario.ALUNO)
                .ativo(true).criadoEm(LocalDateTime.now()).build();
        when(usuarioRepository.salvar(any())).thenReturn(usuarioSalvo);

        Aluno alunoSalvo = Aluno.builder()
                .id(1L).usuarioId(1L).academiaId(1L)
                .peso(new BigDecimal("80.5")).altura(new BigDecimal("1.75"))
                .metaAguaMl(2500).build();
        when(alunoRepository.salvar(any())).thenReturn(alunoSalvo);

        CadastrarAlunoRequest req = CadastrarAlunoRequest.builder()
                .nome("João Silva").email("joao@email.com").senha("Senha123")
                .academiaId(1L).peso(new BigDecimal("80.5"))
                .altura(new BigDecimal("1.75")).metaAguaMl(2500).build();

        AlunoResponse response = service.executar(req);

        assertThat(response.getNome()).isEqualTo("João Silva");
        assertThat(response.getEmail()).isEqualTo("joao@email.com");
        assertThat(response.getMetaAguaMl()).isEqualTo(2500);
        assertThat(response.isAtivo()).isTrue();

        verify(passwordEncoder).encode("Senha123");
        verify(usuarioRepository).salvar(any());
        verify(alunoRepository).salvar(any());
    }

    @Test
    @DisplayName("deve normalizar email para lowercase")
    void deveNormalizarEmail() {
        when(usuarioRepository.existePorEmail("joao@email.com")).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("hash");

        Usuario u = Usuario.builder().id(1L).nome("João").email("joao@email.com")
                .senhaHash("hash").tipo(TipoUsuario.ALUNO).ativo(true)
                .criadoEm(LocalDateTime.now()).build();
        when(usuarioRepository.salvar(any())).thenReturn(u);
        when(alunoRepository.salvar(any())).thenReturn(
                Aluno.builder().id(1L).usuarioId(1L).metaAguaMl(2000).build());

        service.executar(CadastrarAlunoRequest.builder()
                .nome("João").email("JOAO@EMAIL.COM").senha("Senha123").build());

        verify(usuarioRepository).existePorEmail("joao@email.com");
    }

    @Test
    @DisplayName("deve lançar UsuarioJaExisteException quando email duplicado")
    void deveLancarExcecaoEmailDuplicado() {
        when(usuarioRepository.existePorEmail("joao@email.com")).thenReturn(true);

        assertThatThrownBy(() -> service.executar(
                CadastrarAlunoRequest.builder()
                        .nome("João").email("joao@email.com").senha("Senha123").build()))
                .isInstanceOf(UsuarioJaExisteException.class)
                .hasMessageContaining("joao@email.com");

        verify(usuarioRepository, never()).salvar(any());
        verify(alunoRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("deve usar meta de água padrão 2000ml quando não informada")
    void deveUsarMetaAguaPadrao() {
        when(usuarioRepository.existePorEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("hash");
        when(usuarioRepository.salvar(any())).thenReturn(
                Usuario.builder().id(1L).nome("João").email("joao@email.com")
                        .senhaHash("hash").tipo(TipoUsuario.ALUNO).ativo(true)
                        .criadoEm(LocalDateTime.now()).build());
        when(alunoRepository.salvar(argThat(a -> a.getMetaAguaMl() == 2000)))
                .thenReturn(Aluno.builder().id(1L).usuarioId(1L).metaAguaMl(2000).build());

        AlunoResponse resp = service.executar(CadastrarAlunoRequest.builder()
                .nome("João").email("joao@email.com").senha("Senha123").build());

        assertThat(resp.getMetaAguaMl()).isEqualTo(2000);
    }
}
