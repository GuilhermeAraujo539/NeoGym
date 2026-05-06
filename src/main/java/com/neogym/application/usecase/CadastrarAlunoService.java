package com.neogym.application.usecase;

import com.neogym.application.dto.request.CadastrarAlunoRequest;
import com.neogym.application.dto.response.AlunoResponse;
import com.neogym.application.port.in.CadastrarAlunoUseCase;
import com.neogym.application.port.out.AlunoRepositoryPort;
import com.neogym.application.port.out.PasswordEncoderPort;
import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.domain.entity.Aluno;
import com.neogym.domain.entity.Usuario;
import com.neogym.domain.enums.TipoUsuario;
import com.neogym.domain.exception.UsuarioJaExisteException;

import java.time.LocalDateTime;


public class CadastrarAlunoService implements CadastrarAlunoUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final AlunoRepositoryPort   alunoRepository;
    private final PasswordEncoderPort   passwordEncoder;

    public CadastrarAlunoService(
            UsuarioRepositoryPort usuarioRepository,
            AlunoRepositoryPort   alunoRepository,
            PasswordEncoderPort   passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.alunoRepository   = alunoRepository;
        this.passwordEncoder   = passwordEncoder;
    }

    @Override
    public AlunoResponse executar(CadastrarAlunoRequest req) {
        String email = req.getEmail().toLowerCase().strip();

        if (usuarioRepository.existePorEmail(email)) {
            throw new UsuarioJaExisteException(email);
        }

        Usuario usuario = Usuario.builder()
                .nome(req.getNome().strip())
                .email(email)
                .senhaHash(passwordEncoder.encode(req.getSenha()))
                .tipo(TipoUsuario.ALUNO)
                .ativo(true)
                .criadoEm(LocalDateTime.now())
                .build();

        Usuario salvo = usuarioRepository.salvar(usuario);

        Aluno aluno = Aluno.builder()
                .usuarioId(salvo.getId())
                .academiaId(req.getAcademiaId())
                .peso(req.getPeso())
                .altura(req.getAltura())
                .metaAguaMl(req.getMetaAguaMl() != null ? req.getMetaAguaMl() : 2000)
                .build();

        Aluno alunoSalvo = alunoRepository.salvar(aluno);

        return AlunoResponse.builder()
                .id(alunoSalvo.getId())
                .usuarioId(salvo.getId())
                .nome(salvo.getNome())
                .email(salvo.getEmail())
                .academiaId(alunoSalvo.getAcademiaId())
                .peso(alunoSalvo.getPeso())
                .altura(alunoSalvo.getAltura())
                .metaAguaMl(alunoSalvo.getMetaAguaMl())
                .ativo(salvo.isAtivo())
                .criadoEm(salvo.getCriadoEm())
                .build();
    }
}
