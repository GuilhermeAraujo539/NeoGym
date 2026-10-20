package com.neogym.infrastructure.config;

import com.neogym.application.port.in.GerenciarCredenciaisAdminUseCase;
import com.neogym.application.port.in.ObterAdminDashboardUseCase;
import com.neogym.application.port.in.SolicitarVinculoUseCase;
import com.neogym.application.port.out.*;
import com.neogym.application.usecase.*;
import com.neogym.application.usecase.AtualizarFotoPerfilService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.neogym.infrastructure.ai.MockGeradorTreinoIAAdapter;

@Configuration
public class UseCaseConfig {

    @Value("${neogym.jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    @Bean
    public CadastrarAlunoService cadastrarAlunoService(
            UsuarioRepositoryPort usuarioRepository,
            AlunoRepositoryPort   alunoRepository,
            PasswordEncoderPort   passwordEncoder) {
        return new CadastrarAlunoService(usuarioRepository, alunoRepository, passwordEncoder);
    }

    @Bean
    public CadastrarPersonalService cadastrarPersonalService(
            UsuarioRepositoryPort  usuarioRepository,
            PersonalRepositoryPort personalRepository,
            PasswordEncoderPort    passwordEncoder) {
        return new CadastrarPersonalService(usuarioRepository, personalRepository, passwordEncoder);
    }

    @Bean
    public CadastrarNutricionistaService cadastrarNutricionistaService(
            UsuarioRepositoryPort       usuarioRepository,
            NutricionistaRepositoryPort nutricionistaRepository,
            PasswordEncoderPort         passwordEncoder) {
        return new CadastrarNutricionistaService(usuarioRepository, nutricionistaRepository, passwordEncoder);
    }

    @Bean
    public LoginService loginService(
            UsuarioRepositoryPort      usuarioRepository,
            PasswordEncoderPort        passwordEncoder,
            AccessTokenPort            accessTokenPort,
            HashPort                   hashPort,
            RefreshTokenRepositoryPort refreshTokenRepository,
            RequestMetadataPort        requestMetadata) {
        return new LoginService(
                usuarioRepository, passwordEncoder, accessTokenPort,
                hashPort, refreshTokenRepository, requestMetadata,
                refreshTokenExpirationMs);
    }

    @Bean
    public RefreshTokenService refreshTokenService(
            RefreshTokenRepositoryPort refreshTokenRepository,
            UsuarioRepositoryPort      usuarioRepository,
            AccessTokenPort            accessTokenPort,
            HashPort                   hashPort,
            RequestMetadataPort        requestMetadata) {
        return new RefreshTokenService(
                refreshTokenRepository, usuarioRepository, accessTokenPort,
                hashPort, requestMetadata, refreshTokenExpirationMs);
    }

    @Bean
    public LogoutService logoutService(
            RefreshTokenRepositoryPort refreshTokenRepository,
            HashPort                   hashPort) {
        return new LogoutService(refreshTokenRepository, hashPort);
    }
    @Bean
    public GerarTreinoIAService gerarTreinoIAService(
            GeradorTreinoIAPort geradorTreinoIAPort,
            PersonalRepositoryPort personalRepository,
            AlunoRepositoryPort alunoRepository,
            VinculoProfissionalRepositoryPort vinculoRepository) {

        return new GerarTreinoIAService(
                geradorTreinoIAPort,
                personalRepository,
                alunoRepository,
                vinculoRepository
        );
    }

    @Bean
    public CriarFichaTreinoService criarFichaTreinoService(
            PersonalRepositoryPort personalRepository,
            AlunoRepositoryPort alunoRepository,
            PersonalAlunoRepositoryPort personalAlunoRepository,
            FichaTreinoRepositoryPort fichaTreinoRepository
    ) {
        return new CriarFichaTreinoService(
                personalRepository,
                alunoRepository,
                personalAlunoRepository,
                fichaTreinoRepository
        );
    }

    @Bean
    public ListarFichasTreinoService listarFichasTreinoService(
            FichaTreinoRepositoryPort fichaTreinoRepository,
            AlunoRepositoryPort alunoRepository,
            PersonalRepositoryPort personalRepository
    ) {
        return new ListarFichasTreinoService(
                fichaTreinoRepository,
                alunoRepository,
                personalRepository
        );
    }


    @Bean
    public CriarAgendamentoService criarAgendamentoService(
            AlunoRepositoryPort alunoRepository,
            PersonalRepositoryPort personalRepository,
            NutricionistaRepositoryPort nutricionistaRepository,
            PersonalAlunoRepositoryPort personalAlunoRepository,
            AgendamentoRepositoryPort agendamentoRepository,
            UsuarioRepositoryPort usuarioRepository
    ) {
        return new CriarAgendamentoService(
                alunoRepository,
                personalRepository,
                nutricionistaRepository,
                personalAlunoRepository,
                agendamentoRepository,
                usuarioRepository
        );
    }

    @Bean
    public ExcluirAgendamentoService excluirAgendamentoService(
            AgendamentoRepositoryPort agendamentoRepository,
            AlunoRepositoryPort alunoRepository,
            PersonalRepositoryPort personalRepository,
            NutricionistaRepositoryPort nutricionistaRepository
    ) {
        return new ExcluirAgendamentoService(
                agendamentoRepository,
                alunoRepository,
                personalRepository,
                nutricionistaRepository
        );
    }

    @Bean
    public ListarMeusAgendamentosService listarMeusAgendamentosService(
            AgendamentoRepositoryPort agendamentoRepository,
            AlunoRepositoryPort alunoRepository,
            PersonalRepositoryPort personalRepository,
            NutricionistaRepositoryPort nutricionistaRepository,
            UsuarioRepositoryPort usuarioRepository
    ) {
        return new ListarMeusAgendamentosService(
                agendamentoRepository,
                alunoRepository,
                personalRepository,
                nutricionistaRepository,
                usuarioRepository
        );
    }

    @Bean
    public GerenciarPerfilProfissionalService gerenciarPerfilProfissionalService(
            PersonalRepositoryPort personalRepository
    ) {
        return new GerenciarPerfilProfissionalService(personalRepository);
    }

    @Bean
    public GerenciarGaleriaProfissionalService
    gerenciarGaleriaProfissionalService(
            PersonalRepositoryPort personalRepository,
            PersonalFotoRepositoryPort fotoRepository
    ) {
        return new GerenciarGaleriaProfissionalService(
                personalRepository,
                fotoRepository
        );
    }


    @Bean
    public AtualizarPerfilService atualizarPerfilService(
            UsuarioRepositoryPort usuarioRepository
    ) {
        return new AtualizarPerfilService(
                usuarioRepository
        );
    }

    @Bean
    public AtualizarFotoPerfilService atualizarFotoPerfilService(
            UsuarioRepositoryPort usuarioRepository,
            UsuarioFotoRepositoryPort fotoRepository
    ) {
        return new AtualizarFotoPerfilService(
                usuarioRepository,
                fotoRepository
        );
    }



    @Bean
    public ObterAdminDashboardUseCase obterAdminDashboardUseCase(
            AdminDashboardPort adminDashboardPort
    ) {
        return new ObterAdminDashboardUseCase(adminDashboardPort);
    }

    @Bean
    public GerenciarCredenciaisAdminUseCase gerenciarCredenciaisAdminUseCase(
            CredenciaisAdminPort credenciaisAdminPort
    ) {
        return new GerenciarCredenciaisAdminUseCase(
                credenciaisAdminPort
        );
    }

    @Bean
    public SolicitarVinculoUseCase solicitarVinculoUseCase(
            VinculoProfissionalRepositoryPort vinculoRepository,
            UsuarioRepositoryPort usuarioRepository
    ) {
        return new SolicitarVinculoService(
                vinculoRepository,
                usuarioRepository
        );
    }

}
