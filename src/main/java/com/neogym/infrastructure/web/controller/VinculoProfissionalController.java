package com.neogym.infrastructure.web.controller;

import com.neogym.application.dto.request.SolicitarVinculoRequest;
import com.neogym.application.dto.response.AlunoVinculadoResponse;
import com.neogym.application.dto.response.StatusVinculoResponse;
import com.neogym.application.port.in.*;
import com.neogym.domain.entity.VinculoProfissional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vinculos")
@RequiredArgsConstructor
public class VinculoProfissionalController {

    private final SolicitarVinculoUseCase solicitarVinculoUseCase;
    private final BuscarSolicitacoesVinculoUseCase buscarSolicitacoesVinculoUseCase;
    private final ResponderVinculoUseCase responderVinculoUseCase;
    private final BuscarAlunosVinculadosUseCase buscarAlunosVinculadosUseCase;
    private final ConsultarStatusVinculoUseCase consultarStatusVinculoUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VinculoProfissional solicitar(
            Authentication authentication,
            @RequestBody SolicitarVinculoRequest request
    ) {
        return solicitarVinculoUseCase.executar(
                authentication.getName(),
                request
        );
    }

    @GetMapping("/solicitacoes")
    public List<VinculoProfissional> buscarSolicitacoes(
            Authentication authentication
    ) {
        return buscarSolicitacoesVinculoUseCase.executar(
                authentication.getName()
        );
    }

    @PatchMapping("/{id}/aceitar")
    public VinculoProfissional aceitar(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return responderVinculoUseCase.aceitar(
                authentication.getName(),
                id
        );
    }

    @PatchMapping("/{id}/recusar")
    public VinculoProfissional recusar(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return responderVinculoUseCase.recusar(
                authentication.getName(),
                id
        );
    }

    @GetMapping("/alunos")
    public List<AlunoVinculadoResponse> buscarAlunos(
            Authentication authentication
    ) {
        return buscarAlunosVinculadosUseCase.executar(
                authentication.getName()
        );
    }

    @GetMapping("/status/{outroUsuarioId}")
    public StatusVinculoResponse consultarStatus(
            Authentication authentication,
            @PathVariable Long outroUsuarioId
    ) {
        return consultarStatusVinculoUseCase.executar(
                authentication.getName(),
                outroUsuarioId
        );
    }

}