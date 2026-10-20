package com.neogym.infrastructure.persistence.adapter;

import com.neogym.application.port.out.VinculoProfissionalRepositoryPort;
import com.neogym.domain.entity.VinculoProfissional;
import com.neogym.domain.enums.StatusVinculo;
import com.neogym.infrastructure.persistence.entity.VinculoProfissionalJpaEntity;
import com.neogym.infrastructure.persistence.entity.AlunoJpaEntity;
import com.neogym.infrastructure.persistence.entity.UsuarioJpaEntity;
import com.neogym.infrastructure.persistence.mapper.VinculoProfissionalMapper;
import com.neogym.infrastructure.persistence.repository.AlunoJpaRepository;
import com.neogym.infrastructure.persistence.repository.UsuarioJpaRepository;
import com.neogym.infrastructure.persistence.repository.VinculoProfissionalJpaRepository;
import com.neogym.infrastructure.projection.AlunoVinculadoProjection;
import com.neogym.infrastructure.projection.ProfissionalVinculadoProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VinculoProfissionalRepositoryAdapter
        implements VinculoProfissionalRepositoryPort {

    private final VinculoProfissionalJpaRepository repository;
    private final AlunoJpaRepository alunoRepository;
    private final UsuarioJpaRepository usuarioRepository;
    private final VinculoProfissionalMapper mapper;

    @Override
    public VinculoProfissional salvar(VinculoProfissional vinculo) {

        AlunoJpaEntity aluno = alunoRepository
                .findByUsuarioId(vinculo.getAlunoId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        UsuarioJpaEntity profissional = usuarioRepository
                .findById(vinculo.getProfissionalUsuarioId())
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));

        VinculoProfissionalJpaEntity entity =
                VinculoProfissionalJpaEntity.builder()
                        .id(vinculo.getId())
                        .aluno(aluno)
                        .profissional(profissional)
                        .tipoProfissional(vinculo.getTipoProfissional())
                        .status(vinculo.getStatus())
                        .criadoEm(vinculo.getCriadoEm())
                        .atualizadoEm(vinculo.getAtualizadoEm())
                        .build();

        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public VinculoProfissional atualizar(VinculoProfissional vinculo) {

        AlunoJpaEntity aluno = alunoRepository
                .findById(vinculo.getAlunoId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        UsuarioJpaEntity profissional = usuarioRepository
                .findById(vinculo.getProfissionalUsuarioId())
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));

        VinculoProfissionalJpaEntity entity =
                VinculoProfissionalJpaEntity.builder()
                        .id(vinculo.getId())
                        .aluno(aluno)
                        .profissional(profissional)
                        .tipoProfissional(vinculo.getTipoProfissional())
                        .status(vinculo.getStatus())
                        .criadoEm(vinculo.getCriadoEm())
                        .atualizadoEm(vinculo.getAtualizadoEm())
                        .build();

        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public List<VinculoProfissional> buscarSolicitacoesPendentes(
            Long profissionalUsuarioId
    ) {
        return repository
                .findByProfissional_IdAndStatus(
                        profissionalUsuarioId,
                        StatusVinculo.PENDENTE
                )
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<VinculoProfissional> buscarPorAlunoEProfissional(
            Long alunoId,
            Long profissionalUsuarioId
    ) {
        return repository
                .findByAluno_Usuario_IdAndProfissional_Id(
                        alunoId,
                        profissionalUsuarioId
                )
                .map(mapper::toDomain);
    }

    @Override
    public Optional<VinculoProfissional> buscarPorUsuarios(
            Long usuarioId,
            Long outroUsuarioId
    ) {
        return repository
                .findByAluno_Usuario_IdAndProfissional_Id(
                        usuarioId,
                        outroUsuarioId
                )
                .or(() -> repository.findByProfissional_IdAndAluno_Usuario_Id(
                        usuarioId,
                        outroUsuarioId
                ))
                .map(mapper::toDomain);
    }

    @Override
    public Optional<VinculoProfissional> buscarVinculoAceito(
            Long alunoId,
            Long profissionalUsuarioId
    ) {
        return repository
                .findByAluno_IdAndProfissional_IdAndStatus(
                        alunoId,
                        profissionalUsuarioId,
                        StatusVinculo.ACEITO
                )
                .map(mapper::toDomain);
    }


    @Override
    public List<AlunoVinculadoProjection> buscarAlunosVinculados(
            Long profissionalUsuarioId
    ) {
        return repository.buscarAlunosVinculados(
                profissionalUsuarioId,
                StatusVinculo.ACEITO
        );
    }

    @Override
    public List<ProfissionalVinculadoProjection> buscarProfissionaisVinculados(
            Long alunoUsuarioId
    ) {
        return repository.buscarProfissionaisVinculados(
                alunoUsuarioId,
                StatusVinculo.ACEITO
        );
    }

    @Override
    public Optional<VinculoProfissional> buscarPorIdEProfissional(
            Long id,
            Long profissionalUsuarioId
    ) {
        return repository
                .findByIdAndProfissional_Id(id, profissionalUsuarioId)
                .map(mapper::toDomain);
    }
}