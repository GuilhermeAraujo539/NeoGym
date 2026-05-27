package com.neogym.infrastructure.web.controller;

import com.neogym.application.dto.request.AtualizarUsuarioAdminRequest;
import com.neogym.application.dto.response.DashboardAdminResponse;
import com.neogym.application.dto.response.PageResponse;
import com.neogym.application.dto.response.UsuarioResponse;
import com.neogym.application.usecase.DashboardAdminService;
import com.neogym.application.usecase.GerenciarUsuarioAdminService;
import com.neogym.application.usecase.ListarUsuariosAdminService;
import com.neogym.domain.enums.TipoUsuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controller do painel administrativo.
 * TODOS os endpoints exigem role ADMIN — protegido por @PreAuthorize.
 *
 * ROTAS:
 * GET    /api/v1/admin/dashboard              → métricas gerais
 * GET    /api/v1/admin/usuarios               → lista paginada com filtros
 * GET    /api/v1/admin/usuarios/{id}          → detalhes de um usuário
 * PATCH  /api/v1/admin/usuarios/{id}          → editar nome, email, ativar/desativar
 * DELETE /api/v1/admin/usuarios/{id}          → deletar usuário (exceto admins)
 */
@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final DashboardAdminService      dashboardService;
    private final GerenciarUsuarioAdminService gerenciarUsuarioService;
    private final ListarUsuariosAdminService listarUsuariosService;
    private final UsuarioResolverHelper      usuarioResolver;

    // ── Dashboard ──────────────────────────────────────────────────────────

    /**
     * GET /api/v1/admin/dashboard
     * Retorna totais: usuários por tipo, credenciais por status, vínculos pendentes.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardAdminResponse> dashboard() {
        return ResponseEntity.ok(dashboardService.executar());
    }

    // ── Usuários ───────────────────────────────────────────────────────────

    /**
     * GET /api/v1/admin/usuarios?tipo=PERSONAL&ativo=true&pagina=0&tamanho=20
     * Lista usuários com filtros opcionais de tipo e status.
     */
    @GetMapping("/usuarios")
    public ResponseEntity<PageResponse<UsuarioResponse>> listarUsuarios(
            @RequestParam(required = false) TipoUsuario tipo,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(defaultValue = "0")  int pagina,
            @RequestParam(defaultValue = "20") int tamanho,
            @RequestParam(defaultValue = "criadoEm") String ordenarPor,
            @RequestParam(defaultValue = "desc") String direcao) {

        Sort sort = direcao.equalsIgnoreCase("asc")
                ? Sort.by(Sort.Direction.ASC, ordenarPor)
                : Sort.by(Sort.Direction.DESC, ordenarPor);

        return ResponseEntity.ok(listarUsuariosService.listar(
                tipo, ativo, PageRequest.of(pagina, tamanho, sort)));
    }

    /**
     * GET /api/v1/admin/usuarios/{id}
     * Retorna detalhes completos de um usuário específico.
     */
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(listarUsuariosService.buscarPorId(id));
    }

    /**
     * PATCH /api/v1/admin/usuarios/{id}
     * Atualiza nome, e-mail ou status ativo/inativo.
     * Desativar revoga todas as sessões do usuário imediatamente.
     */
    @PatchMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponse> atualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarUsuarioAdminRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long adminId = usuarioResolver.resolverId(userDetails.getUsername());
        return ResponseEntity.ok(
                gerenciarUsuarioService.atualizarUsuario(id, adminId, request));
    }

    /**
     * DELETE /api/v1/admin/usuarios/{id}
     * Deleta permanentemente um usuário (não admins).
     * Revoga todas as sessões antes de deletar.
     */
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> deletarUsuario(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long adminId = usuarioResolver.resolverId(userDetails.getUsername());
        gerenciarUsuarioService.deletarUsuario(id, adminId);
        return ResponseEntity.noContent().build();
    }
}
