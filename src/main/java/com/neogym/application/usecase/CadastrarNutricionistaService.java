package com.neogym.application.usecase;

import com.neogym.application.dto.request.CadastrarNutricionistaRequest;
import com.neogym.application.dto.response.NutricionistaResponse;
import com.neogym.application.port.in.CadastrarNutricionistaUseCase;
import com.neogym.application.port.out.CredencialRepositoryPort;
import com.neogym.application.port.out.NutricionistaRepositoryPort;
import com.neogym.application.port.out.PasswordEncoderPort;
import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.domain.entity.Credencial;
import com.neogym.domain.entity.Nutricionista;
import com.neogym.domain.entity.Usuario;
import com.neogym.domain.enums.StatusCredencial;
import com.neogym.domain.enums.TipoUsuario;
import com.neogym.domain.exception.UsuarioJaExisteException;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

public class CadastrarNutricionistaService
        implements CadastrarNutricionistaUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final NutricionistaRepositoryPort nutricionistaRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final CredencialRepositoryPort credencialRepository;

    public CadastrarNutricionistaService(
            UsuarioRepositoryPort usuarioRepository,
            NutricionistaRepositoryPort nutricionistaRepository,
            PasswordEncoderPort passwordEncoder,
            CredencialRepositoryPort credencialRepository) {

        this.usuarioRepository = usuarioRepository;
        this.nutricionistaRepository = nutricionistaRepository;
        this.passwordEncoder = passwordEncoder;
        this.credencialRepository = credencialRepository;
    }

    @Override
    @Transactional
    public NutricionistaResponse executar(
            CadastrarNutricionistaRequest req,
            String arquivoIdentidade,
            String arquivoProfissional) {

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
                .estadoCrn(req.getEstadoCrn().toUpperCase().strip())
                .statusCrn(StatusCredencial.PENDENTE)
                .build();

        Nutricionista nutriSalva = nutricionistaRepository.salvar(nutri);

        // Registra os documentos para análise administrativa.
        Credencial credencial = Credencial.builder()
                .usuarioId(salvo.getId())
                .tipo("CRN")
                .arquivoUrl(arquivoProfissional)
                .arquivoIdentidadeUrl(arquivoIdentidade)
                .status(StatusCredencial.PENDENTE)
                .criadoEm(LocalDateTime.now())
                .build();

        credencialRepository.salvar(credencial);

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
