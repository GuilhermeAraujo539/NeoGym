package com.neogym.application.dto.request;

public record AtualizarUsuarioAdminRequest(
        String nome,
        String email,
        Boolean ativo
) {}