package com.neogym.application.dto.response;

public record AdminDashboardResponse(
        long totalUsuarios,
        long totalAlunos,
        long totalPersonais,
        long totalNutricionistas,
        long totalAcademias,
        long usuariosAtivos,
        long usuariosInativos,
        long credenciaisPendentes,
        long credenciaisAprovadas,
        long credenciaisRejeitadas,
        long vinculosPendentes
) {}