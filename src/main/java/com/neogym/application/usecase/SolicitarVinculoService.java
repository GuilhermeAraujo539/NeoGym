package com.neogym.application.usecase;

import com.neogym.application.dto.request.SolicitarVinculoRequest;
import com.neogym.application.port.in.SolicitarVinculoUseCase;
import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.application.port.out.VinculoProfissionalRepositoryPort;
import com.neogym.domain.entity.Usuario;
import com.neogym.domain.entity.VinculoProfissional;
import com.neogym.domain.enums.StatusVinculo;
import com.neogym.domain.enums.TipoUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SolicitarVinculoService implements SolicitarVinculoUseCase {

    private final VinculoProfissionalRepositoryPort vinculoRepository;
    private final UsuarioRepositoryPort usuarioRepository;

    @Override
    public VinculoProfissional executar(
            String email,
            SolicitarVinculoRequest request
    ) {

        Usuario aluno = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (aluno.getTipo() != TipoUsuario.ALUNO) {
            throw new IllegalArgumentException(
                    "Apenas alunos podem solicitar vínculo"
            );
        }

        if (request.getTipoProfissional() != TipoUsuario.PERSONAL
                && request.getTipoProfissional() != TipoUsuario.NUTRICIONISTA) {

            throw new IllegalArgumentException(
                    "Tipo de profissional inválido"
            );
        }

        Usuario profissional = usuarioRepository
                .buscarPorId(request.getProfissionalUsuarioId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Profissional não encontrado"
                        )
                );

        if (profissional.getTipo() != request.getTipoProfissional()) {
            throw new IllegalArgumentException(
                    "O tipo do profissional não corresponde ao usuário"
            );
        }

        if (!profissional.isAtivo()) {
            throw new IllegalArgumentException(
                    "Profissional inativo"
            );
        }

        vinculoRepository.buscarPorAlunoEProfissional(
                aluno.getId(),
                request.getProfissionalUsuarioId()
        ).ifPresent(vinculo -> {
            throw new IllegalArgumentException(
                    "Já existe um vínculo com este profissional"
            );
        });

        VinculoProfissional vinculo = VinculoProfissional.builder()
                .alunoId(aluno.getId())
                .profissionalUsuarioId(
                        request.getProfissionalUsuarioId()
                )
                .tipoProfissional(
                        request.getTipoProfissional()
                )
                .status(StatusVinculo.PENDENTE)
                .criadoEm(LocalDateTime.now())
                .build();

        return vinculoRepository.salvar(vinculo);
    }

}