package com.neogym.application.usecase;

import com.neogym.application.dto.request.CadastrarPersonalRequest;
import com.neogym.application.dto.response.PersonalResponse;
import com.neogym.application.port.in.CadastrarPersonalUseCase;
import com.neogym.application.port.out.PasswordEncoderPort;
import com.neogym.application.port.out.PersonalRepositoryPort;
import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.domain.entity.Personal;
import com.neogym.domain.entity.Usuario;
import com.neogym.domain.enums.StatusCredencial;
import com.neogym.domain.enums.TipoUsuario;
import com.neogym.domain.exception.UsuarioJaExisteException;

import java.time.LocalDateTime;

public class CadastrarPersonalService implements CadastrarPersonalUseCase {

    private final UsuarioRepositoryPort  usuarioRepository;
    private final PersonalRepositoryPort personalRepository;
    private final PasswordEncoderPort    passwordEncoder;

    public CadastrarPersonalService(
            UsuarioRepositoryPort  usuarioRepository,
            PersonalRepositoryPort personalRepository,
            PasswordEncoderPort    passwordEncoder) {
        this.usuarioRepository  = usuarioRepository;
        this.personalRepository = personalRepository;
        this.passwordEncoder    = passwordEncoder;
    }

    @Override
    public PersonalResponse executar(CadastrarPersonalRequest req) {
        String email = req.getEmail().toLowerCase().strip();

        if (usuarioRepository.existePorEmail(email)) {
            throw new UsuarioJaExisteException(email);
        }

        Usuario usuario = Usuario.builder()
                .nome(req.getNome().strip())
                .email(email)
                .senhaHash(passwordEncoder.encode(req.getSenha()))
                .tipo(TipoUsuario.PERSONAL)
                .ativo(true)
                .criadoEm(LocalDateTime.now())
                .build();

        Usuario salvo = usuarioRepository.salvar(usuario);

        Personal personal = Personal.builder()
                .usuarioId(salvo.getId())
                .cref(req.getCref().toUpperCase().strip())
                .estadoCref(req.getEstadoCref().toUpperCase())
                .statusCref(StatusCredencial.PENDENTE)
                .build();

        Personal personalSalvo = personalRepository.salvar(personal);

        return PersonalResponse.builder()
                .id(personalSalvo.getId())
                .usuarioId(salvo.getId())
                .nome(salvo.getNome())
                .email(salvo.getEmail())
                .cref(personalSalvo.getCref())
                .estadoCref(personalSalvo.getEstadoCref())
                .statusCref(personalSalvo.getStatusCref())
                .ativo(salvo.isAtivo())
                .criadoEm(salvo.getCriadoEm())
                .build();
    }
}
