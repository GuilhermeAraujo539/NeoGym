package com.neogym.infrastructure.persistence.adapter;

import com.neogym.application.dto.response.AdminDashboardResponse;
import com.neogym.application.port.out.AdminDashboardPort;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDashboardPersistenceAdapter implements AdminDashboardPort {

    private final JdbcTemplate jdbcTemplate;

    public AdminDashboardPersistenceAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SQL = """
        SELECT
            (SELECT COUNT(*)
             FROM usuario) AS total_usuarios,

            (SELECT COUNT(*)
             FROM usuario
             WHERE tipo::text = 'ALUNO') AS total_alunos,

            (SELECT COUNT(*)
             FROM usuario
             WHERE tipo::text = 'PERSONAL') AS total_personais,

            (SELECT COUNT(*)
             FROM usuario
             WHERE tipo::text = 'NUTRICIONISTA') AS total_nutricionistas,

            (SELECT COUNT(*)
             FROM academia) AS total_academias,

            (SELECT COUNT(*)
             FROM usuario
             WHERE ativo = TRUE) AS usuarios_ativos,

            (SELECT COUNT(*)
             FROM usuario
             WHERE ativo = FALSE) AS usuarios_inativos,

            (SELECT COUNT(*)
             FROM credencial
             WHERE status::text = 'PENDENTE') AS credenciais_pendentes,

            (SELECT COUNT(*)
             FROM credencial
             WHERE status::text = 'APROVADO') AS credenciais_aprovadas,

            (SELECT COUNT(*)
             FROM credencial
             WHERE status::text = 'REJEITADO') AS credenciais_rejeitadas,

            (SELECT COUNT(*)
             FROM personal_academia
             WHERE status::text = 'PENDENTE') AS vinculos_pendentes
        """;

    @Override
    public AdminDashboardResponse buscarIndicadores() {
        return jdbcTemplate.queryForObject(SQL, (rs, rowNum) ->
                new AdminDashboardResponse(
                        rs.getLong("total_usuarios"),
                        rs.getLong("total_alunos"),
                        rs.getLong("total_personais"),
                        rs.getLong("total_nutricionistas"),
                        rs.getLong("total_academias"),
                        rs.getLong("usuarios_ativos"),
                        rs.getLong("usuarios_inativos"),
                        rs.getLong("credenciais_pendentes"),
                        rs.getLong("credenciais_aprovadas"),
                        rs.getLong("credenciais_rejeitadas"),
                        rs.getLong("vinculos_pendentes")
                )
        );
    }
}