package com.neogym.infrastructure.persistence.adapter;

import com.neogym.application.dto.response.AdminUsuarioResponse;
import com.neogym.application.port.out.AdminUsuariosPort;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AdminUsuariosPersistenceAdapter implements AdminUsuariosPort {

    private final JdbcTemplate jdbcTemplate;

    public AdminUsuariosPersistenceAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String FROM_WHERE = """
        FROM usuario u
        WHERE 1 = 1
        """;

    private final RowMapper<AdminUsuarioResponse> rowMapper =
            (rs, rowNum) -> {
                Timestamp criado = rs.getTimestamp("criado_em");

                return new AdminUsuarioResponse(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("tipo"),
                        rs.getBoolean("ativo"),
                        criado != null ? criado.toLocalDateTime() : null
                );
            };

    @Override
    public List<AdminUsuarioResponse> listar(
            String tipo,
            Boolean ativo,
            long offset,
            int tamanho
    ) {
        StringBuilder sql = new StringBuilder("""
            SELECT
                u.id,
                u.nome,
                u.email,
                u.tipo::text AS tipo,
                u.ativo,
                u.criado_em
            """);

        sql.append(FROM_WHERE);

        List<Object> parametros = new ArrayList<>();
        adicionarFiltros(sql, parametros, tipo, ativo);

        sql.append("""
            ORDER BY u.criado_em DESC, u.id DESC
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
    public long contar(String tipo, Boolean ativo) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) ");
        sql.append(FROM_WHERE);

        List<Object> parametros = new ArrayList<>();
        adicionarFiltros(sql, parametros, tipo, ativo);

        Long total = jdbcTemplate.queryForObject(
                sql.toString(),
                Long.class,
                parametros.toArray()
        );

        return total != null ? total : 0L;
    }

    private void adicionarFiltros(
            StringBuilder sql,
            List<Object> parametros,
            String tipo,
            Boolean ativo
    ) {
        if (tipo != null && !tipo.isBlank()) {
            sql.append(" AND u.tipo::text = ? ");
            parametros.add(tipo);
        }

        if (ativo != null) {
            sql.append(" AND u.ativo = ? ");
            parametros.add(ativo);
        }
    }

    @Override
    @Transactional
    public ResultadoAtualizacao atualizar(
            long id,
            String nome,
            String email,
            Boolean ativo
    ) {
        List<String> tipos = jdbcTemplate.queryForList(
                "SELECT tipo::text FROM usuario WHERE id = ?",
                String.class,
                id
        );

        if (tipos.isEmpty()) {
            return ResultadoAtualizacao.NAO_ENCONTRADO;
        }

        String tipoUsuario = tipos.get(0);

        if ("ADMIN".equals(tipoUsuario) && Boolean.FALSE.equals(ativo)) {
            return ResultadoAtualizacao.ADMIN_PROTEGIDO;
        }

        List<String> campos = new ArrayList<>();
        List<Object> parametros = new ArrayList<>();

        if (nome != null) {
            campos.add("nome = ?");
            parametros.add(nome);
        }

        if (email != null) {
            List<Long> emailsEncontrados = jdbcTemplate.queryForList(
                    """
                    SELECT id
                    FROM usuario
                    WHERE email = ?
                      AND id <> ?
                    """,
                    Long.class,
                    email,
                    id
            );

            if (!emailsEncontrados.isEmpty()) {
                return ResultadoAtualizacao.EMAIL_EM_USO;
            }

            campos.add("email = ?");
            parametros.add(email);
        }

        if (ativo != null) {
            campos.add("ativo = ?");
            parametros.add(ativo);
        }

        if (campos.isEmpty()) {
            return ResultadoAtualizacao.NENHUMA_ALTERACAO;
        }

        String sql = "UPDATE usuario SET "
                + String.join(", ", campos)
                + " WHERE id = ?";

        parametros.add(id);

        int atualizados = jdbcTemplate.update(
                sql,
                parametros.toArray()
        );

        return atualizados > 0
                ? ResultadoAtualizacao.ATUALIZADO
                : ResultadoAtualizacao.NAO_ENCONTRADO;
    }

    @Override
    @Transactional
    public ResultadoExclusao excluir(long id) {
        List<String> tipos = jdbcTemplate.queryForList(
                "SELECT tipo::text FROM usuario WHERE id = ?",
                String.class,
                id
        );

        if (tipos.isEmpty()) {
            return ResultadoExclusao.NAO_ENCONTRADO;
        }

        if ("ADMIN".equals(tipos.get(0))) {
            return ResultadoExclusao.ADMIN_PROTEGIDO;
        }

        /*
         * Verifica referências com restrições ON DELETE RESTRICT:
         * sessões do aluno, sessões como profissional, mensagens
         * enviadas e fichas de treino criadas por personal.
         */
        Long dependencias = jdbcTemplate.queryForObject(
                """
                SELECT
                    (SELECT COUNT(*)
                     FROM sessao s
                     JOIN aluno a ON a.id = s.aluno_id
                     WHERE a.usuario_id = ?)
                  + (SELECT COUNT(*)
                     FROM sessao
                     WHERE profissional_id = ?)
                  + (SELECT COUNT(*)
                     FROM mensagem
                     WHERE remetente_id = ?)
                  + (SELECT COUNT(*)
                     FROM ficha_treino ft
                     JOIN personal p ON p.id = ft.personal_id
                     WHERE p.usuario_id = ?)
                """,
                Long.class,
                id, id, id, id
        );

        if (dependencias != null && dependencias > 0) {
            return ResultadoExclusao.POSSUI_VINCULOS;
        }

        int removidos = jdbcTemplate.update(
                """
                DELETE FROM usuario
                WHERE id = ?
                  AND tipo::text <> 'ADMIN'
                """,
                id
        );

        return removidos > 0
                ? ResultadoExclusao.EXCLUIDO
                : ResultadoExclusao.NAO_ENCONTRADO;
    }
}