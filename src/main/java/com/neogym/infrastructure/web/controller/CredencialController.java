package com.neogym.infrastructure.web.controller;

import com.neogym.application.dto.request.AvaliarCredencialRequest;
import com.neogym.application.dto.response.CredencialResponse;
import com.neogym.application.dto.response.PageResponse;
import com.neogym.application.usecase.AvaliarCredencialService;
import com.neogym.application.usecase.EnviarCredencialService;
import com.neogym.application.usecase.ListarCredenciaisService;
import com.neogym.domain.enums.StatusCredencial;
import com.neogym.domain.enums.TipoCredencial;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller de credenciais.
 *
 * ROTAS:
 * POST   /api/v1/credenciais                     → profissional envia documento
 * GET    /api/v1/credenciais                     → admin lista todas (com filtro)
 * GET    /api/v1/credenciais/minhas              → profissional vê sua própria
 * PATCH  /api/v1/credenciais/{id}/avaliar        → admin aprova ou rejeita
 *
 * SEGURANÇA:
 * – Upload: apenas PERSONAL e NUTRICIONISTA
 * – Listar/Avaliar: apenas ADMIN
 * – Cada profissional só vê sua própria credencial
 */
@RestController
@RequestMapping("/api/v1/credenciais")
@RequiredArgsConstructor
public class CredencialController {

    private final EnviarCredencialService   enviarCredencialService;
    private final AvaliarCredencialService  avaliarCredencialService;
    private final ListarCredenciaisService  listarCredenciaisService;
    private final UsuarioResolverHelper     usuarioResolver;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('PERSONAL','NUTRICIONISTA')")
    public ResponseEntity<CredencialResponse> enviar(
            @RequestParam("arquivo") MultipartFile arquivo,
            @RequestParam("tipo") TipoCredencial tipo,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long usuarioId = usuarioResolver.resolverId(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enviarCredencialService.executar(usuarioId, tipo, arquivo));
    }


    @GetMapping("/minhas")
    @PreAuthorize("hasAnyRole('PERSONAL','NUTRICIONISTA')")
    public ResponseEntity<PageResponse<CredencialResponse>> minhas(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0")  int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {

        Long usuarioId = usuarioResolver.resolverId(userDetails.getUsername());
        StatusCredencial status = null;
        return ResponseEntity.ok(listarCredenciaisService
                .listarPorUsuario(usuarioId, PageRequest.of(pagina, tamanho,
                        Sort.by(Sort.Direction.DESC, "criadoEm"))));
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<CredencialResponse>> listar(
            @RequestParam(required = false) StatusCredencial status,
            @RequestParam(defaultValue = "0")  int pagina,
            @RequestParam(defaultValue = "20") int tamanho) {

        return ResponseEntity.ok(listarCredenciaisService.listarPorStatus(
                status,
                PageRequest.of(pagina, tamanho, Sort.by(Sort.Direction.DESC, "criadoEm"))));
    }


    @PatchMapping("/{id}/avaliar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CredencialResponse> avaliar(
            @PathVariable Long id,
            @Valid @RequestBody AvaliarCredencialRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long adminId = usuarioResolver.resolverId(userDetails.getUsername());
        return ResponseEntity.ok(avaliarCredencialService.executar(id, adminId, request));
    }
}
