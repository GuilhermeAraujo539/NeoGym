package com.neogym.infrastructure.projection;

public interface ProfissionalProximoProjection {

    Long getId();

    Long getUsuarioId();

    String getNome();

    String getEmail();

    String getTipo();

    Double getDistanciaKm();

    String getEspecialidade();

    String getApresentacao();

    String getAreasAtuacao();

    String getHorariosDisponiveis();
}