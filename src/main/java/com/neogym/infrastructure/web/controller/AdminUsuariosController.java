package com.neogym.infrastructure.web.controller;

import com.neogym.application.dto.request.AtualizarUsuarioAdminRequest;
import com.neogym.application.dto.response.AdminUsuariosPaginaResponse;
import com.neogym.application.port.in.GerenciarUsuariosAdminUseCase;
import com.neogym.application.port.out.AdminUsuariosPort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/admin/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsuariosController {

    private static final Set<String> TIPOS = Set.of(
            "ALUNO",
            "PERSONAL",
            "NUTRICIONISTA",
            "ACADEMIA",
            "ADMIN"
    );

    private final GerenciarUsuariosAdminUseCase useCase;

    public AdminUsuariosController(
            GerenciarUsuariosAdminUseCase useCase
    ) {
        this.useCase = useCase;
    }

    @GetMapping
    public ResponseEntity<?> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanho
    ) {
        if (pagina < 0 || tamanho < 1 || tamanho > 100) {
            return ResponseEntity.badRequest().body(
                    Map.of("mensagem", "Parâmetros de paginação inválidos.")
            );
        }

        String filtroTipo = tipo == null || tipo.isBlank()
                ? null
                : tipo.trim().toUpperCase();

        if (filtroTipo != null && !TIPOS.contains(filtroTipo)) {
            return ResponseEntity.badRequest().body(
                    Map.of("mensagem", "Tipo de usuário inválido.")
            );
        }

        AdminUsuariosPaginaResponse resposta = useCase.listar(
                filtroTipo,
                ativo,
                pagina,
                tamanho
        );

        return ResponseEntity.ok(resposta);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable long id,
            @RequestBody AtualizarUsuarioAdminRequest request
    ) {
        if (id <= 0 || request == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("mensagem", "Dados inválidos.")
            );
        }

        String nome = request.nome() == null
                ? null
                : request.nome().trim();

        String email = request.email() == null
                ? null
                : request.email().trim();

        if (nome != null && (nome.isBlank() || nome.length() > 120)) {
            return ResponseEntity.badRequest().body(
                    Map.of("mensagem", "Informe um nome com até 120 caracteres.")
            );
        }

        if (email != null &&
                (email.isBlank()
                        || email.length() > 120
                        || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))) {
            return ResponseEntity.badRequest().body(
                    Map.of("mensagem", "Informe um e-mail válido.")
            );
        }

        var resultado = useCase.atualizar(
                id,
                nome,
                email,
                request.ativo()
        );

        return switch (resultado) {
            case ATUALIZADO ->
                    ResponseEntity.noContent().build();

            case NAO_ENCONTRADO ->
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                            Map.of("mensagem", "Usuário não encontrado.")
                    );

            case EMAIL_EM_USO ->
                    ResponseEntity.status(HttpStatus.CONFLICT).body(
                            Map.of("mensagem", "Este e-mail já está cadastrado.")
                    );

            case ADMIN_PROTEGIDO ->
                    ResponseEntity.status(HttpStatus.CONFLICT).body(
                            Map.of(
                                    "mensagem",
                                    "Não é permitido desativar uma conta administradora."
                            )
                    );

            case NENHUMA_ALTERACAO ->
                    ResponseEntity.badRequest().body(
                            Map.of("mensagem", "Nenhuma alteração foi informada.")
                    );
        };
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().body(
                    Map.of("mensagem", "ID inválido.")
            );
        }

        var resultado = useCase.excluir(id);

        return switch (resultado) {
            case EXCLUIDO ->
                    ResponseEntity.noContent().build();

            case NAO_ENCONTRADO ->
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                            Map.of("mensagem", "Usuário não encontrado.")
                    );

            case ADMIN_PROTEGIDO ->
                    ResponseEntity.status(HttpStatus.CONFLICT).body(
                            Map.of(
                                    "mensagem",
                                    "Não é permitido excluir administradores."
                            )
                    );

            case POSSUI_VINCULOS ->
                    ResponseEntity.status(HttpStatus.CONFLICT).body(
                            Map.of(
                                    "mensagem",
                                    "Este usuário possui sessões, mensagens ou fichas de treino vinculadas. Desative a conta em vez de excluí-la."
                            )
                    );
        };
    }
}