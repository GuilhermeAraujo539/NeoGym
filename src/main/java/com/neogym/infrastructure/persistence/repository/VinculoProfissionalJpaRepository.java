package com.neogym.infrastructure.persistence.repository;

import com.neogym.domain.enums.StatusVinculo;
import com.neogym.infrastructure.persistence.entity.VinculoProfissionalJpaEntity;
import com.neogym.infrastructure.projection.AlunoVinculadoProjection;
import com.neogym.infrastructure.projection.ProfissionalVinculadoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VinculoProfissionalJpaRepository
        extends JpaRepository<VinculoProfissionalJpaEntity, Long> {

    Optional<VinculoProfissionalJpaEntity> findByAluno_Usuario_IdAndProfissional_Id(
            Long alunoUsuarioId,
            Long profissionalId
    );

    Optional<VinculoProfissionalJpaEntity> findByProfissional_IdAndAluno_Usuario_Id(
            Long profissionalUsuarioId,
            Long alunoUsuarioId
    );

    List<VinculoProfissionalJpaEntity> findByProfissional_IdAndStatus(
            Long profissionalId,
            StatusVinculo status
    );

    Optional<VinculoProfissionalJpaEntity> findByAluno_IdAndProfissional_IdAndStatus(
            Long alunoId,
            Long profissionalUsuarioId,
            StatusVinculo status
    );

    Optional<VinculoProfissionalJpaEntity> findByIdAndProfissional_Id(
            Long id,
            Long profissionalId
    );


    @Query("""
    SELECT
        v.id AS vinculoId,
        a.id AS alunoId,
        u.id AS usuarioId,
        u.nome AS nome,
        u.email AS email
    FROM VinculoProfissionalJpaEntity v
    JOIN v.aluno a
    JOIN a.usuario u
    WHERE v.profissional.id = :profissionalUsuarioId
      AND v.status = :status
    ORDER BY u.nome
""")
    List<AlunoVinculadoProjection> buscarAlunosVinculados(
            @Param("profissionalUsuarioId") Long profissionalUsuarioId,
            @Param("status") StatusVinculo status
    );


    @Query("""
    SELECT
        v.id AS vinculoId,
        u.id AS usuarioId,
        u.nome AS nome,
        u.email AS email
    FROM VinculoProfissionalJpaEntity v
    JOIN v.aluno a
    JOIN v.profissional u
    WHERE a.usuario.id = :alunoUsuarioId
      AND v.status = :status
    ORDER BY u.nome
""")
    List<ProfissionalVinculadoProjection> buscarProfissionaisVinculados(
            @Param("alunoUsuarioId") Long alunoUsuarioId,
            @Param("status") StatusVinculo status
    );

}