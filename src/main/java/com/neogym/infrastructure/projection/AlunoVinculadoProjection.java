package com.neogym.infrastructure.projection;

public interface AlunoVinculadoProjection {

    Long getVinculoId();

    Long getAlunoId();

    Long getUsuarioId();

    String getNome();

    String getEmail();
}
