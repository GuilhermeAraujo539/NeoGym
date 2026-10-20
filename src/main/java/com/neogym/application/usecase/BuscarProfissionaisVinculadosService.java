package com.neogym.application.usecase;

import com.neogym.application.dto.response.ProfissionalVinculadoResponse;
import com.neogym.application.port.in.BuscarProfissionaisVinculadosUseCase;
import com.neogym.application.port.out.UsuarioRepositoryPort;
import com.neogym.application.port.out.VinculoProfissionalRepositoryPort;
import com.neogym.domain.enums.TipoUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarProfissionaisVinculadosService
        implements BuscarProfissionaisVinculadosUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final VinculoProfissionalRepositoryPort vinculoRepository;

    @Override
    public List<ProfissionalVinculadoResponse> executar(String email) {

        var usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado"));

        if (usuario.getTipo() != TipoUsuario.ALUNO) {
            throw new RuntimeException(
                    "Apenas alunos podem consultar seus Personais"
            );
        }

        return vinculoRepository
                .buscarProfissionaisVinculados(usuario.getId())
                .stream()
                .map(p -> ProfissionalVinculadoResponse.builder()
                        .vinculoId(p.getVinculoId())
                        .usuarioId(p.getUsuarioId())
                        .nome(p.getNome())
                        .email(p.getEmail())
                        .build())
                .toList();
    }
}