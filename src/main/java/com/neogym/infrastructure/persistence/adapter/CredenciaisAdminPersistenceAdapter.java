package com.neogym.infrastructure.persistence.adapter;

import com.neogym.application.dto.response.CredencialAdminResponse;
import com.neogym.application.port.out.CredenciaisAdminPort;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CredenciaisAdminPersistenceAdapter
        implements CredenciaisAdminPort {

    private final JdbcTemplate jdbcTemplate;

    public CredenciaisAdminPersistenceAdapter(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String FROM_WHERE = """
        FROM credencial c
        JOIN usuario u ON u.id = c.usuario_id
        WHERE u.tipo::text IN ('PERSONAL', 'NUTRICIONISTA')
        """;

    private final RowMapper<CredencialAdminResponse> rowMapper =
            (rs, rowNum) -> {
                Timestamp criado = rs.getTimestamp("criado_em");
                Timestamp avaliado = rs.getTimestamp("avaliado_em");

                return new CredencialAdminResponse(
                        rs.getLong("id"),
                        rs.getString("nome_usuario"),
                        rs.getString("email_usuario"),
                        rs.getString("tipo"),
                        rs.getString("status"),
                        rs.getString("arquivo_url"),
                        criado != null ? criado.toLocalDateTime() : null,
                        avaliado != null ? avaliado.toLocalDateTime() : null,
                        rs.getString("observacao_admin")
                );
            };

    @Override
    public List<CredencialAdminResponse> listar(
            String status,
            long offset,
            int tamanho
    ) {
        StringBuilder sql = new StringBuilder("""
            SELECT
                c.id,
                u.nome AS nome_usuario,
                u.email AS email_usuario,
                c.tipo,
                c.status::text AS status,
                c.arquivo_url,
                c.criado_em,
                c.avaliado_em,
                c.observacao_admin
            """);

        sql.append(FROM_WHERE);

        List<Object> parametros = new ArrayList<>();

        adicionarFiltroStatus(sql, parametros, status);

        sql.append("""
            ORDER BY
                CASE WHEN c.status::text = 'PENDENTE' THEN 0 ELSE 1 END,
                c.criado_em DESC,
                c.id DESC
            LIMIT ?
            OFFSET ?
            """);

        parametros.add(tamanho);
        parametros.add(offset);

        return jdbcTemplate.query(
                sql.toString(),
                rowMapper,
                parametros.toArray()
        );
    }

    @Override
    public long contar(String status) {
        StringBuilder sql = new StringBuilder("""
            SELECT COUNT(*)
            """);

        sql.append(FROM_WHERE);

        List<Object> parametros = new ArrayList<>();

        adicionarFiltroStatus(sql, parametros, status);

        Long total = jdbcTemplate.queryForObject(
                sql.toString(),
                Long.class,
                parametros.toArray()
        );

        return total != null ? total : 0L;
    }

    private void adicionarFiltroStatus(
            StringBuilder sql,
            List<Object> parametros,
            String status
    ) {
        if (status != null && !status.isBlank()) {
            sql.append(" AND c.status::text = ? ");
            parametros.add(status);
        }
    }

    private record CredencialAlvo(
            long usuarioId,
            String tipo,
            String status
    ) {}

    @Override
    @Transactional
    public ResultadoAvaliacao avaliar(
            long id,
            String status,
            String observacao
    ) {
        List<CredencialAlvo> encontrados = jdbcTemplate.query(
                """
                SELECT
                    usuario_id,
                    tipo,
                    status::text AS status
                FROM credencial
                WHERE id = ?
                """,
                (rs, rowNum) -> new CredencialAlvo(
                        rs.getLong("usuario_id"),
                        rs.getString("tipo"),
                        rs.getString("status")
                ),
                id
        );

        if (encontrados.isEmpty()) {
            return ResultadoAvaliacao.NAO_ENCONTRADA;
        }

        CredencialAlvo credencial = encontrados.get(0);

        if (!"PENDENTE".equals(credencial.status())) {
            return ResultadoAvaliacao.JA_AVALIADA;
        }

        int atualizados = jdbcTemplate.update(
                """
                UPDATE credencial
                SET status = ?::status_credencial,
                    avaliado_em = NOW(),
                    observacao_admin = ?
                WHERE id = ?
                  AND status::text = 'PENDENTE'
                """,
                status,
                observacao,
                id
        );

        // Protege contra duas avaliações simultâneas.
        if (atualizados == 0) {
            return ResultadoAvaliacao.JA_AVALIADA;
        }

        sincronizarStatusProfissional(
                credencial.usuarioId(),
                credencial.tipo(),
                status
        );

        return ResultadoAvaliacao.ATUALIZADA;
    }

    private void sincronizarStatusProfissional(
            long usuarioId,
            String tipo,
            String status
    ) {
        if ("CREF".equalsIgnoreCase(tipo)
                || "PERSONAL".equalsIgnoreCase(tipo)) {

            jdbcTemplate.update(
                    """
                    UPDATE personal
                    SET status_cref = ?::status_credencial
                    WHERE usuario_id = ?
                    """,
                    status,
                    usuarioId
            );

        } else if ("CRN".equalsIgnoreCase(tipo)
                || "NUTRICIONISTA".equalsIgnoreCase(tipo)) {

            jdbcTemplate.update(
                    """
                    UPDATE nutricionista
                    SET status_crn = ?::status_credencial
                    WHERE usuario_id = ?
                    """,
                    status,
                    usuarioId
            );
        }
    }
}