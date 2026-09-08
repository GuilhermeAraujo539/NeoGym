package com.neogym.infrastructure.web.controller;

import com.neogym.application.dto.request.GerarTreinoIARequest;
import com.neogym.application.dto.response.TreinoIAResponse;
import com.neogym.application.port.in.GerarTreinoIAUseCase;
import com.neogym.infrastructure.security.service.AuthenticatedUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ia")
@RequiredArgsConstructor
public class InteligenciaArtificialController {

    private final GerarTreinoIAUseCase gerarTreinoIAUseCase;

    @PostMapping("/treino")
    public ResponseEntity<TreinoIAResponse> gerarTreino(
            @Valid @RequestBody GerarTreinoIARequest request,
            Authentication authentication) {

        AuthenticatedUser usuario =
                (AuthenticatedUser) authentication.getPrincipal();

        Long usuarioId = usuario.getUsuarioId();

        return ResponseEntity.ok(
                gerarTreinoIAUseCase.executar(
                        usuarioId,
                        request
                )
        );
    }
}