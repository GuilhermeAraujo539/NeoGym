package com.neogym.infrastructure.web.controller;

import com.neogym.application.dto.request.CriarFichaTreinoRequest;
import com.neogym.application.dto.response.FichaTreinoResponse;
import com.neogym.application.port.in.CriarFichaTreinoUseCase;
import com.neogym.application.port.in.ListarFichasTreinoUseCase;
import com.neogym.infrastructure.security.service.AuthenticatedUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/fichas-treino")
@RequiredArgsConstructor
public class FichaTreinoController {

    private final CriarFichaTreinoUseCase criarFichaTreinoUseCase;
    private final ListarFichasTreinoUseCase listarFichasTreinoUseCase;

    @PostMapping
    public ResponseEntity<FichaTreinoResponse> criar(
            @Valid @RequestBody CriarFichaTreinoRequest request,
            Authentication authentication
    ) {

        AuthenticatedUser usuario =
                (AuthenticatedUser) authentication.getPrincipal();

        return ResponseEntity.ok(
                criarFichaTreinoUseCase.executar(
                        usuario.getUsuarioId(),
                        request
                )
        );
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<FichaTreinoResponse>> listarPorAluno(
            @PathVariable Long alunoId,
            Authentication authentication
    ) {
        boolean ehPersonal = authentication.getAuthorities()
                .stream()
                .anyMatch(a ->
                        "ROLE_PERSONAL".equals(a.getAuthority())
                );

        if (!ehPersonal) {
            return ResponseEntity.status(
                    org.springframework.http.HttpStatus.FORBIDDEN
            ).build();
        }

        AuthenticatedUser usuario =
                (AuthenticatedUser) authentication.getPrincipal();

        return ResponseEntity.ok(
                listarFichasTreinoUseCase.executarParaPersonal(
                        usuario.getUsuarioId(),
                        alunoId
                )
        );
    }

    @GetMapping("/minhas")
    public ResponseEntity<List<FichaTreinoResponse>> listarMinhas(
            Authentication authentication
    ) {
        AuthenticatedUser usuario =
                (AuthenticatedUser) authentication.getPrincipal();

        return ResponseEntity.ok(
                listarFichasTreinoUseCase.executarPorUsuarioId(
                        usuario.getUsuarioId()
                )
        );
    }
}