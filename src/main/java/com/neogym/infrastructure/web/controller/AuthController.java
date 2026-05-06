package com.neogym.infrastructure.web.controller;

import com.neogym.application.dto.request.*;
import com.neogym.application.dto.response.*;
import com.neogym.application.port.in.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/cadastro/aluno")
    public ResponseEntity<AlunoResponse> cadastrarAluno(
            @Valid @RequestBody CadastrarAlunoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cadastrarAlunoUseCase.executar(request));
    }

    @PostMapping("/cadastro/personal")
    public ResponseEntity<PersonalResponse> cadastrarPersonal(
            @Valid @RequestBody CadastrarPersonalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cadastrarPersonalUseCase.executar(request));
    }

    @PostMapping("/cadastro/nutricionista")
    public ResponseEntity<NutricionistaResponse> cadastrarNutricionista(
            @Valid @RequestBody CadastrarNutricionistaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cadastrarNutricionistaUseCase.executar(request));
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
