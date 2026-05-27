package com.neogym.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DashboardAdminResponse {
    private long totalUsuarios;
    private long totalAlunos;
    private long totalPersonais;
    private long totalNutricionistas;
    private long totalAcademias;
    private long credenciaisPendentes;
    private long credenciaisAprovadas;
    private long credenciaisRejeitadas;
    private long usuariosAtivos;
    private long usuariosInativos;
    private long vinculosPendentes;
}
