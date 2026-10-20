package com.neogym.infrastructure.web.controller;

import com.neogym.application.dto.request.AtualizarPerfilProfissionalRequest;
import com.neogym.application.dto.response.PerfilProfissionalResponse;
import com.neogym.application.port.in.GerenciarPerfilProfissionalUseCase;
import com.neogym.infrastructure.security.service.AuthenticatedUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/personais/me")
@RequiredArgsConstructor
public class PerfilProfissionalController {

    private final GerenciarPerfilProfissionalUseCase useCase;

    @GetMapping
    public ResponseEntity<PerfilProfissionalResponse> buscar(
            Authentication authentication
    ) {
        if (!ehPersonal(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        AuthenticatedUser usuario =
                (AuthenticatedUser) authentication.getPrincipal();

        return ResponseEntity.ok(
                useCase.buscar(usuario.getUsuarioId())
        );
    }

    @PutMapping
    public ResponseEntity<PerfilProfissionalResponse> atualizar(
            @Valid @RequestBody AtualizarPerfilProfissionalRequest request,
            Authentication authentication
    ) {
        if (!ehPersonal(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        AuthenticatedUser usuario =
                (AuthenticatedUser) authentication.getPrincipal();

        return ResponseEntity.ok(
                useCase.atualizar(usuario.getUsuarioId(), request)
        );
    }

    private boolean ehPersonal(Authentication authentication) {
        return authentication != null
                && authentication.getAuthorities().stream()
                .anyMatch(a ->
                        "ROLE_PERSONAL".equals(a.getAuthority())
                );
    }
}