package com.neogym.infrastructure.web.handler;

import com.neogym.domain.exception.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ── Validação de campos (@Valid) ──────────────────────────────────────────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        List<ApiError.CampoErro> campos = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> ApiError.CampoErro.builder()
                        .campo(e.getField())
                        .mensagem(e.getDefaultMessage())
                        .build())
                .toList();

        return build(HttpStatus.BAD_REQUEST, "Dados inválidos",
                "Verifique os campos informados.", campos);
    }

    // ── Exceções de negócio ──────────────────────────────────────────────────

    @ExceptionHandler(UsuarioJaExisteException.class)
    public ResponseEntity<ApiError> handleUsuarioJaExiste(UsuarioJaExisteException ex) {
        return build(HttpStatus.CONFLICT, "Conflito", ex.getMessage(), null);
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<ApiError> handleCredenciaisInvalidas(CredenciaisInvalidasException ex) {
        // Não revelamos se o e-mail existe — mensagem genérica intencional
        return build(HttpStatus.UNAUTHORIZED, "Não autorizado", ex.getMessage(), null);
    }

    @ExceptionHandler(RefreshTokenInvalidoException.class)
    public ResponseEntity<ApiError> handleRefreshTokenInvalido(RefreshTokenInvalidoException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Não autorizado", ex.getMessage(), null);
    }

    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<ApiError> handleAcessoNegado(AcessoNegadoException ex) {
        return build(HttpStatus.FORBIDDEN, "Acesso negado", ex.getMessage(), null);
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ApiError> handleUsuarioNaoEncontrado(UsuarioNaoEncontradoException ex) {
        return build(HttpStatus.NOT_FOUND, "Não encontrado", ex.getMessage(), null);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, "Não encontrado", ex.getMessage(), null);
    }

    // ── Spring Security ──────────────────────────────────────────────────────

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthentication(AuthenticationException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Não autorizado",
                "Token inválido ou expirado.", null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex) {
        return build(HttpStatus.FORBIDDEN, "Acesso negado",
                "Você não tem permissão para acessar este recurso.", null);
    }

    // ── Erros de tipo/parâmetro ──────────────────────────────────────────────

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String msg = "Parâmetro '%s' com valor inválido: '%s'".formatted(ex.getName(), ex.getValue());
        return build(HttpStatus.BAD_REQUEST, "Parâmetro inválido", msg, null);
    }

    // ── Catch-all ────────────────────────────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        log.error("Erro interno não tratado: {}", ex.getMessage(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno",
                "Ocorreu um erro inesperado. Tente novamente mais tarde.", null);
    }

    // ── Helper ───────────────────────────────────────────────────────────────

    private ResponseEntity<ApiError> build(
            HttpStatus status, String erro, String mensagem,
            List<ApiError.CampoErro> campos) {

        ApiError body = ApiError.builder()
                .status(status.value())
                .erro(erro)
                .mensagem(mensagem)
                .timestamp(LocalDateTime.now())
                .campos(campos)
                .build();

        return ResponseEntity.status(status).body(body);
    }
}
