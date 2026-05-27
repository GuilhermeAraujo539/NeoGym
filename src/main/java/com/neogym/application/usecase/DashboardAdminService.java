package com.neogym.application.usecase;

import com.neogym.application.dto.response.DashboardAdminResponse;
import com.neogym.application.port.out.AdminRepositoryPort;
import com.neogym.application.port.out.CredencialRepositoryPort;
import com.neogym.domain.enums.StatusCredencial;
import com.neogym.domain.enums.TipoUsuario;

public class DashboardAdminService {

    private final AdminRepositoryPort      adminRepository;
    private final CredencialRepositoryPort credencialRepository;

    public DashboardAdminService(AdminRepositoryPort      adminRepository,
                                 CredencialRepositoryPort credencialRepository) {
        this.adminRepository      = adminRepository;
        this.credencialRepository = credencialRepository;
    }

    public DashboardAdminResponse executar() {
        return DashboardAdminResponse.builder()
                .totalUsuarios(adminRepository.contarTotalUsuarios())
                .totalAlunos(adminRepository.contarUsuariosPorTipo(TipoUsuario.ALUNO))
                .totalPersonais(adminRepository.contarUsuariosPorTipo(TipoUsuario.PERSONAL))
                .totalNutricionistas(adminRepository.contarUsuariosPorTipo(TipoUsuario.NUTRICIONISTA))
                .totalAcademias(adminRepository.contarUsuariosPorTipo(TipoUsuario.ACADEMIA))
                .credenciaisPendentes(credencialRepository.contarPorStatus(StatusCredencial.PENDENTE))
                .credenciaisAprovadas(credencialRepository.contarPorStatus(StatusCredencial.APROVADO))
                .credenciaisRejeitadas(credencialRepository.contarPorStatus(StatusCredencial.REJEITADO))
                .usuariosAtivos(adminRepository.contarUsuariosPorAtivo(true))
                .usuariosInativos(adminRepository.contarUsuariosPorAtivo(false))
                .vinculosPendentes(adminRepository.contarVinculosPendentes())
                .build();
    }
}
