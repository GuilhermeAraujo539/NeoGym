package com.neogym.application.usecase;

import com.neogym.application.dto.response.AlunoVinculadoResponse;
import com.neogym.application.port.in.BuscarAlunosVinculadosUseCase;
import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.application.port.out.VinculoProfissionalRepositoryPort;
import com.neogym.domain.entity.Usuario;
import com.neogym.domain.enums.TipoUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarAlunosVinculadosService
        implements BuscarAlunosVinculadosUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final VinculoProfissionalRepositoryPort vinculoRepository;

    @Override
    public List<AlunoVinculadoResponse> executar(String email) {

        Usuario profissional = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Usuário não encontrado"));

        if (profissional.getTipo() != TipoUsuario.PERSONAL
                && profissional.getTipo() != TipoUsuario.NUTRICIONISTA) {
            throw new IllegalArgumentException(
                    "Apenas profissionais podem consultar alunos");
        }

        return vinculoRepository
                .buscarAlunosVinculados(profissional.getId())
                .stream()
                .map(aluno -> AlunoVinculadoResponse.builder()
                        .vinculoId(aluno.getVinculoId())
                        .alunoId(aluno.getAlunoId())
                        .usuarioId(aluno.getUsuarioId())
                        .nome(aluno.getNome())
                        .email(aluno.getEmail())
                        .build())
                .toList();
    }
}
