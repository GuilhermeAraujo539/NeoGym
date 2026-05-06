package com.neogym.application.usecase;

import com.neogym.application.dto.request.CadastrarNutricionistaRequest;
import com.neogym.application.dto.response.NutricionistaResponse;
import com.neogym.application.port.in.CadastrarNutricionistaUseCase;
import com.neogym.application.port.out.NutricionistaRepositoryPort;
import com.neogym.application.port.out.PasswordEncoderPort;
import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.domain.entity.Nutricionista;
import com.neogym.domain.entity.Usuario;
import com.neogym.domain.enums.StatusCredencial;
import com.neogym.domain.enums.TipoUsuario;
import com.neogym.domain.exception.UsuarioJaExisteException;

import java.time.LocalDateTime;

public class CadastrarNutricionistaService implements CadastrarNutricionistaUseCase {

    private final UsuarioRepositoryPort       usuarioRepository;
    private final NutricionistaRepositoryPort nutricionistaRepository;
    private final PasswordEncoderPort         passwordEncoder;

    public CadastrarNutricionistaService(
            UsuarioRepositoryPort       usuarioRepository,
            NutricionistaRepositoryPort nutricionistaRepository,
            PasswordEncoderPort         passwordEncoder) {
        this.usuarioRepository       = usuarioRepository;
        this.nutricionistaRepository = nutricionistaRepository;
        this.passwordEncoder         = passwordEncoder;
    }

    @Override
    public NutricionistaResponse executar(CadastrarNutricionistaRequest req) {
        String email = req.getEmail().toLowerCase().strip();

        if (usuarioRepository.existePorEmail(email)) {
            throw new UsuarioJaExisteException(email);
        }

        Usuario usuario = Usuario.builder()
                .nome(req.getNome().strip())
                .email(email)
                .senhaHash(passwordEncoder.encode(req.getSenha()))
                .tipo(TipoUsuario.NUTRICIONISTA)
                .ativo(true)
                .criadoEm(LocalDateTime.now())
                .build();

        Usuario salvo = usuarioRepository.salvar(usuario);

        Nutricionista nutri = Nutricionista.builder()
                .usuarioId(salvo.getId())
                .crn(req.getCrn().toUpperCase().strip())
                .estadoCrn(req.getEstadoCrn().toUpperCase())
                .statusCrn(StatusCredencial.PENDENTE)
                .build();

        Nutricionista nutriSalva = nutricionistaRepository.salvar(nutri);

        return NutricionistaResponse.builder()
                .id(nutriSalva.getId())
                .usuarioId(salvo.getId())
                .nome(salvo.getNome())
                .email(salvo.getEmail())
                .crn(nutriSalva.getCrn())
                .estadoCrn(nutriSalva.getEstadoCrn())
                .statusCrn(nutriSalva.getStatusCrn())
                .ativo(salvo.isAtivo())
                .criadoEm(salvo.getCriadoEm())
                .build();
    }
}
