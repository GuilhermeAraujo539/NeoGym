package com.neogym.application.port.in;
import com.neogym.application.dto.response.AdminDashboardResponse;
import com.neogym.application.port.out.AdminDashboardPort;

public class ObterAdminDashboardUseCase {

    private final AdminDashboardPort adminDashboardPort;

    public ObterAdminDashboardUseCase(
            AdminDashboardPort adminDashboardPort
    ) {
        this.adminDashboardPort = adminDashboardPort;
    }

    public AdminDashboardResponse executar() {
        return adminDashboardPort.buscarIndicadores();
    }
}