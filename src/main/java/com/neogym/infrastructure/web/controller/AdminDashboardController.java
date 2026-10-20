package com.neogym.infrastructure.web.controller;

import com.neogym.application.dto.response.AdminDashboardResponse;

import com.neogym.application.port.in.ObterAdminDashboardUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminDashboardController {

    private final ObterAdminDashboardUseCase obterAdminDashboardUseCase;

    public AdminDashboardController(
            ObterAdminDashboardUseCase obterAdminDashboardUseCase
    ) {
        this.obterAdminDashboardUseCase = obterAdminDashboardUseCase;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDashboardResponse> buscarDashboard() {
        return ResponseEntity.ok(
                obterAdminDashboardUseCase.executar()
        );
    }
}