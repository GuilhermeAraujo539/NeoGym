package com.neogym.infrastructure.config;

import com.neogym.application.port.out.*;
import com.neogym.application.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class UseCaseAdminConfig {

    @Bean
    public EnviarCredencialService enviarCredencialService(
            CredencialRepositoryPort credencialRepository,
            UsuarioRepositoryPort    usuarioRepository,
            FileStoragePort          fileStorage) {
        return new EnviarCredencialService(credencialRepository, usuarioRepository, fileStorage);
    }

    @Bean
    public AvaliarCredencialService avaliarCredencialService(
            CredencialRepositoryPort    credencialRepository,
            UsuarioRepositoryPort       usuarioRepository,
            PersonalRepositoryPort      personalRepository,
            NutricionistaRepositoryPort nutricionistaRepository) {
        return new AvaliarCredencialService(
                credencialRepository, usuarioRepository,
                personalRepository, nutricionistaRepository);
    }

    @Bean
    public ListarCredenciaisService listarCredenciaisService(
            CredencialRepositoryPort credencialRepository,
            UsuarioRepositoryPort    usuarioRepository) {
        return new ListarCredenciaisService(credencialRepository, usuarioRepository);
    }

    @Bean
    public DashboardAdminService dashboardAdminService(
            AdminRepositoryPort      adminRepository,
            CredencialRepositoryPort credencialRepository) {
        return new DashboardAdminService(adminRepository, credencialRepository);
    }

    @Bean
    public GerenciarUsuarioAdminService gerenciarUsuarioAdminService(
            UsuarioRepositoryPort      usuarioRepository,
            RefreshTokenRepositoryPort refreshTokenRepository) {
        return new GerenciarUsuarioAdminService(usuarioRepository, refreshTokenRepository);
    }

    @Bean
    public ListarUsuariosAdminService listarUsuariosAdminService(
            UsuarioRepositoryPort usuarioRepository) {
        return new ListarUsuariosAdminService(usuarioRepository);
    }
}
