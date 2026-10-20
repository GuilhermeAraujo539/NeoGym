package com.neogym.infrastructure.web.controller;

import com.neogym.application.dto.request.AvaliarCredencialRequest;
import com.neogym.application.dto.response.CredenciaisAdminPaginaResponse;
import com.neogym.application.port.in.GerenciarCredenciaisAdminUseCase;
import com.neogym.application.port.out.CredenciaisAdminPort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/credenciais")
@PreAuthorize("hasRole('ADMIN')")
public class CredenciaisAdminController {

    private static final Set<String> STATUS_LISTAGEM = Set.of(
            "PENDENTE",
            "APROVADO",
            "REJEITADO"
    );

    private static final Set<String> STATUS_AVALIACAO = Set.of(
            "APROVADO",
            "REJEITADO"
    );

    private final GerenciarCredenciaisAdminUseCase useCase;

    public CredenciaisAdminController(
            GerenciarCredenciaisAdminUseCase useCase
    ) {
        this.useCase = useCase;
    }

    @GetMapping
    public ResponseEntity<?> listar(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanho
    ) {
        if (pagina < 0 || tamanho < 1 || tamanho > 100) {
            return ResponseEntity.badRequest().body(
                    Map.of("mensagem", "Parâmetros de paginação inválidos.")
            );
        }

        String filtro = status == null || status.isBlank()
                ? null
                : status.trim();

        if (filtro != null && !STATUS_LISTAGEM.contains(filtro)) {
            return ResponseEntity.badRequest().body(
                    Map.of("mensagem", "Status de credencial inválido.")
            );
        }

        CredenciaisAdminPaginaResponse resposta = useCase.listar(
                filtro,
                pagina,
                tamanho
        );

        return ResponseEntity.ok(resposta);
    }

    @PatchMapping("/{id}/avaliar")
    public ResponseEntity<?> avaliar(
            @PathVariable long id,
            @RequestBody AvaliarCredencialRequest request
    ) {
        if (id <= 0 || request.status() == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("mensagem", "Dados da avaliação inválidos.")
            );
        }

        String status = request.status().trim();

        if (!STATUS_AVALIACAO.contains(status)) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "mensagem",
                            "A avaliação deve ser APROVADO ou REJEITADO."
                    )
            );
        }

        String observacao = request.observacao();

        if (observacao != null) {
            observacao = observacao.trim();

            if (observacao.isEmpty()) {
                observacao = null;
            }
        }

        if (observacao != null && observacao.length() > 500) {
            return ResponseEntity.badRequest().body(
                    Map.of("mensagem", "A observação deve ter até 500 caracteres.")
            );
        }

        if ("REJEITADO".equals(status)
                && (observacao == null || observacao.isBlank())) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "mensagem",
                            "Informe o motivo da rejeição."
                    )
            );
        }

        CredenciaisAdminPort.ResultadoAvaliacao resultado =
                useCase.avaliar(id, status, observacao);

        if (resultado == CredenciaisAdminPort.ResultadoAvaliacao.NAO_ENCONTRADA) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("mensagem", "Credencial não encontrada.")
            );
        }

        if (resultado == CredenciaisAdminPort.ResultadoAvaliacao.JA_AVALIADA) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("mensagem", "Esta credencial já foi avaliada.")
            );
        }

        return ResponseEntity.ok().build();
    }
}