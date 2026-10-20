package com.neogym.application.port.out;

import com.neogym.domain.entity.VinculoProfissional;
import com.neogym.infrastructure.projection.AlunoVinculadoProjection;
import com.neogym.infrastructure.projection.ProfissionalVinculadoProjection;

import java.util.List;
import java.util.Optional;

public interface VinculoProfissionalRepositoryPort {

    VinculoProfissional salvar(VinculoProfissional vinculo);

    Optional<VinculoProfissional> buscarPorAlunoEProfissional(
            Long alunoId,
            Long profissionalUsuarioId
    );

    List<VinculoProfissional> buscarSolicitacoesPendentes(
            Long profissionalUsuarioId
    );

    Optional<VinculoProfissional> buscarPorIdEProfissional(
            Long id,
            Long profissionalUsuarioId
    );

    List<AlunoVinculadoProjection> buscarAlunosVinculados(
            Long profissionalUsuarioId
    );

    Optional<VinculoProfissional> buscarPorUsuarios(
            Long usuarioId,
            Long outroUsuarioId
    );

    Optional<VinculoProfissional> buscarVinculoAceito(
            Long alunoId,
            Long profissionalUsuarioId
    );

    List<ProfissionalVinculadoProjection> buscarProfissionaisVinculados(
            Long alunoUsuarioId
    );

    VinculoProfissional atualizar(VinculoProfissional vinculo);
}