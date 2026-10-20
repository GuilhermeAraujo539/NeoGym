package com.neogym.infrastructure.persistence.repository;

import com.neogym.infrastructure.projection.ProfissionalProximoProjection;
import com.neogym.infrastructure.persistence.entity.PersonalJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfissionalJpaRepository
        extends JpaRepository<PersonalJpaEntity, Long> {

    @Query(value = """
    SELECT
        p.id AS id,
        u.id AS usuarioId,
        p.especialidade AS "especialidade",
        p.apresentacao AS "apresentacao",
        p.areas_atuacao AS "areasAtuacao",
        p.horarios_disponiveis AS "horariosDisponiveis",
        u.nome AS nome,
        u.email AS email,
        'PERSONAL' AS tipo,

        ST_Distance(
            a.localizacao,
            CAST(
                ST_SetSRID(
                    ST_MakePoint(:longitude, :latitude),
                    4326
                ) AS geography
            )
        ) / 1000.0 AS distanciaKm

    FROM personal p

    JOIN usuario u
        ON u.id = p.usuario_id

    JOIN personal_academia pa
        ON pa.personal_id = p.id

    JOIN academia a
        ON a.id = pa.academia_id

    WHERE
        u.ativo = true
        AND a.ativo = true
        AND pa.status = 'APROVADO'

        AND ST_DWithin(
            a.localizacao,
            CAST(
                ST_SetSRID(
                    ST_MakePoint(:longitude, :latitude),
                    4326
                ) AS geography
            ),
            (:raioKm * 1000)
        )

    ORDER BY distanciaKm ASC
    """,
            countQuery = """
    SELECT COUNT(*)

    FROM personal p

    JOIN usuario u
        ON u.id = p.usuario_id

    JOIN personal_academia pa
        ON pa.personal_id = p.id

    JOIN academia a
        ON a.id = pa.academia_id

    WHERE
        u.ativo = true
        AND a.ativo = true
        AND pa.status = 'APROVADO'

        AND ST_DWithin(
            a.localizacao,
            CAST(
                ST_SetSRID(
                    ST_MakePoint(:longitude, :latitude),
                    4326
                ) AS geography
            ),
            (:raioKm * 1000)
        )
    """,
            nativeQuery = true
    )
    Page<ProfissionalProximoProjection> buscarPersonalsProximos(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("raioKm") Double raioKm,
            Pageable pageable
    );
}