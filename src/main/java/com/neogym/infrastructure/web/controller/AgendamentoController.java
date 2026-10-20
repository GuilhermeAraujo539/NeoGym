package com.neogym.infrastructure.web.controller;

import com.neogym.application.dto.request.CriarAgendamentoRequest;
import com.neogym.application.dto.response.AgendamentoResponse;
import com.neogym.application.port.in.CriarAgendamentoUseCase;
import com.neogym.application.port.in.ExcluirAgendamentoUseCase;
import com.neogym.application.port.in.ListarMeusAgendamentosUseCase;
import com.neogym.infrastructure.security.service.AuthenticatedUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final CriarAgendamentoUseCase criarAgendamentoUseCase;
    private final ListarMeusAgendamentosUseCase listarMeusAgendamentosUseCase;
    private final ExcluirAgendamentoUseCase excluirAgendamentoUseCase;

    @PostMapping
    public ResponseEntity<AgendamentoResponse> criar(
            @Valid @RequestBody CriarAgendamentoRequest request,
            Authentication authentication
    ) {

        AuthenticatedUser usuario =
                (AuthenticatedUser)
                        authentication.getPrincipal();

        String tipoUsuario =
                authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Tipo de usuário não identificado"
                                )
                        );

        return ResponseEntity.ok(
                criarAgendamentoUseCase.executar(
                        usuario.getUsuarioId(),
                        tipoUsuario,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id,
            Authentication authentication
    ) {
        AuthenticatedUser usuario =
                (AuthenticatedUser) authentication.getPrincipal();

        String tipoUsuario = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(
                                "Tipo de usuário não identificado"
                        )
                );

        excluirAgendamentoUseCase.executar(
                usuario.getUsuarioId(),
                tipoUsuario,
                id
        );

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<AgendamentoResponse>> listarPorAluno(
            @PathVariable Long alunoId,
            Authentication authentication
    ) {
        boolean ehPersonal = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> "ROLE_PERSONAL".equals(a.getAuthority()));

        if (!ehPersonal) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        AuthenticatedUser usuario =
                (AuthenticatedUser) authentication.getPrincipal();

        String tipoUsuario = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter("ROLE_PERSONAL"::equals)
                .findFirst()
                .orElseThrow();

        List<AgendamentoResponse> agendamentos =
                listarMeusAgendamentosUseCase.executar(
                        usuario.getUsuarioId(),
                        tipoUsuario
                );

        List<AgendamentoResponse> filtrados = agendamentos.stream()
                .filter(a -> Objects.equals(a.getAlunoId(), alunoId))
                .toList();

        return ResponseEntity.ok(filtrados);
    }

    @GetMapping("/minhas")
    public ResponseEntity<List<AgendamentoResponse>> listarMinhas(
            Authentication authentication
    ) {

            AuthenticatedUser usuario =
                (AuthenticatedUser)
                        authentication.getPrincipal();

        String tipoUsuario =
                authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Tipo de usuário não identificado"
                                )
                        );

        return ResponseEntity.ok(
                listarMeusAgendamentosUseCase.executar(
                        usuario.getUsuarioId(),
                        tipoUsuario
                )
        );
    }
}