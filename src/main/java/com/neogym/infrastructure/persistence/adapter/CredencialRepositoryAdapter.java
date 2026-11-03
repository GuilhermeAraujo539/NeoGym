package com.neogym.infrastructure.persistence.adapter;

import com.neogym.application.port.out.CredencialRepositoryPort;
import com.neogym.domain.entity.Credencial;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Repository
public class CredencialRepositoryAdapter
        implements CredencialRepositoryPort {

    private final JdbcTemplate jdbcTemplate;

    public CredencialRepositoryAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void salvar(Credencial credencial) {
        String sql = """
                INSERT INTO credencial (
                    usuario_id,
                    tipo,
                    arquivo_url,
                    arquivo_identidade_url,
                    status,
                    criado_em
                )
                VALUES (?, ?, ?, ?, ?::status_credencial, ?)
                """;

        LocalDateTime criadoEm = credencial.getCriadoEm() != null
                ? credencial.getCriadoEm()
                : LocalDateTime.now();

        jdbcTemplate.update(
                sql,
                credencial.getUsuarioId(),
                credencial.getTipo(),
                credencial.getArquivoUrl(),
                credencial.getArquivoIdentidadeUrl(),
                credencial.getStatus().name(),
                Timestamp.valueOf(criadoEm)
        );
    }

    @Override
    public Optional<String> buscarNomeArquivo(Long id, String tipo) {
        String coluna = switch (tipo.toUpperCase(Locale.ROOT)) {
            case "IDENTIDADE" -> "arquivo_identidade_url";
            case "PROFISSIONAL" -> "arquivo_url";
            default -> throw new IllegalArgumentException(
                    "Tipo de documento inválido."
            );
        };

        String sql = "SELECT " + coluna
                + " FROM credencial WHERE id = ?";

        return jdbcTemplate.query(
                sql,
                rs -> {
                    if (!rs.next()) {
                        return Optional.empty();
                    }

                    String nomeArquivo = rs.getString(1);

                    if (nomeArquivo == null || nomeArquivo.isBlank()) {
                        return Optional.empty();
                    }

                    return Optional.of(nomeArquivo);
                },
                id
        );
    }
}