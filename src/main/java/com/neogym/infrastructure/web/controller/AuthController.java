package com.neogym.infrastructure.web.controller;

import com.neogym.application.dto.request.*;
import com.neogym.application.dto.response.*;
import com.neogym.application.port.in.*;
import com.neogym.infrastructure.storage.CredencialArquivoStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CadastrarAlunoUseCase         cadastrarAlunoUseCase;
    private final CadastrarPersonalUseCase      cadastrarPersonalUseCase;
    private final CadastrarNutricionistaUseCase cadastrarNutricionistaUseCase;
    private final LoginUseCase                  loginUseCase;
    private final RefreshTokenUseCase           refreshTokenUseCase;
    private final LogoutUseCase                 logoutUseCase;
    private final CredencialArquivoStorageService credencialArquivoStorageService;

    @PostMapping("/cadastro/aluno")
    public ResponseEntity<AlunoResponse> cadastrarAluno(
            @Valid @RequestBody CadastrarAlunoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cadastrarAlunoUseCase.executar(request));
    }

    @PostMapping(
            value = "/cadastro/personal",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> cadastrarPersonal(
            @Valid @RequestPart("dados") CadastrarPersonalRequest request,
            @RequestPart("documentoIdentidade") MultipartFile identidade,
            @RequestPart("documentoProfissional") MultipartFile documento
    ) throws IOException {

        String arquivoIdentidade = null;
        String arquivoProfissional = null;

        try {
            arquivoIdentidade =
                    credencialArquivoStorageService.salvar(identidade);

            arquivoProfissional =
                    credencialArquivoStorageService.salvar(documento);

            PersonalResponse resposta =
                    cadastrarPersonalUseCase.executar(
                            request,
                            arquivoIdentidade,
                            arquivoProfissional
                    );

            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);

        } catch (IllegalArgumentException e) {
            excluirArquivoSeExistir(arquivoIdentidade);
            excluirArquivoSeExistir(arquivoProfissional);

            return ResponseEntity.badRequest().body(
                    Map.of("message", e.getMessage())
            );

        } catch (IOException | RuntimeException e) {
            excluirArquivoSeExistir(arquivoIdentidade);
            excluirArquivoSeExistir(arquivoProfissional);

            throw e;
        }
    }

    @PostMapping(
            value = "/cadastro/nutricionista",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> cadastrarNutricionista(
            @Valid @RequestPart("dados") CadastrarNutricionistaRequest request,
            @RequestPart("documentoIdentidade") MultipartFile identidade,
            @RequestPart("documentoProfissional") MultipartFile documento
    ) throws IOException {

        String arquivoIdentidade = null;
        String arquivoProfissional = null;

        try {
            arquivoIdentidade =
                    credencialArquivoStorageService.salvar(identidade);

            arquivoProfissional =
                    credencialArquivoStorageService.salvar(documento);

            NutricionistaResponse resposta =
                    cadastrarNutricionistaUseCase.executar(
                            request,
                            arquivoIdentidade,
                            arquivoProfissional
                    );

            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);

        } catch (IllegalArgumentException e) {
            excluirArquivoSeExistir(arquivoIdentidade);
            excluirArquivoSeExistir(arquivoProfissional);

            return ResponseEntity.badRequest().body(
                    Map.of("message", e.getMessage())
            );

        } catch (IOException | RuntimeException e) {
            excluirArquivoSeExistir(arquivoIdentidade);
            excluirArquivoSeExistir(arquivoProfissional);

            throw e;
        }
    }

    private void excluirArquivoSeExistir(String nomeArquivo) {
        if (nomeArquivo == null || nomeArquivo.isBlank()) {
            return;
        }

        try {
            credencialArquivoStorageService.excluir(nomeArquivo);
        } catch (IOException ignored) {
            // Não substitui a exceção original caso a limpeza falhe.
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginUseCase.executar(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(refreshTokenUseCase.executar(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Valid @RequestBody RefreshTokenRequest request) {
        logoutUseCase.executar(request);
        return ResponseEntity.noContent().build();
    }
}
