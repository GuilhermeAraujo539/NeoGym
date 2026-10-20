package com.neogym.infrastructure.web.controller;

import com.neogym.application.dto.request.AtualizarPerfilRequest;
import com.neogym.application.dto.response.PerfilResponse;
import com.neogym.application.port.in.AtualizarPerfilUseCase;
import com.neogym.application.port.in.AtualizarFotoPerfilUseCase;
import com.neogym.application.port.out.UsuarioFotoRepositoryPort;
import com.neogym.infrastructure.security.service.AuthenticatedUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final AtualizarPerfilUseCase atualizarPerfilUseCase;
    private final UsuarioFotoRepositoryPort usuarioFotoRepository;
    private final AtualizarFotoPerfilUseCase atualizarFotoPerfilUseCase;
    private final UsuarioFotoRepositoryPort fotoRepository;

    @PutMapping("/me")
    public ResponseEntity<PerfilResponse> atualizarPerfil(
            @Valid @RequestBody AtualizarPerfilRequest request,
            Authentication authentication
    ) {

        AuthenticatedUser usuario =
                (AuthenticatedUser) authentication.getPrincipal();

        return ResponseEntity.ok(
                atualizarPerfilUseCase.executar(
                        usuario.getUsuarioId(),
                        request
                )
        );
    }

    @PostMapping(
            value = "/me/foto",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> atualizarFoto(
            @RequestParam("foto") MultipartFile foto,
            Authentication authentication
    ) throws IOException {

        AuthenticatedUser usuario =
                (AuthenticatedUser) authentication.getPrincipal();

        if (foto.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        atualizarFotoPerfilUseCase.executar(
                usuario.getUsuarioId(),
                foto.getBytes(),
                foto.getContentType()
        );

        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/foto")
    public ResponseEntity<byte[]> buscarFoto(
            Authentication authentication
    ) {

        AuthenticatedUser usuario =
                (AuthenticatedUser) authentication.getPrincipal();

        return usuarioFotoRepository
                .buscar(usuario.getUsuarioId())
                .map(foto ->
                        ResponseEntity.ok()
                                .contentType(
                                        MediaType.parseMediaType(
                                                foto.contentType()
                                        )
                                )
                                .body(foto.dados())
                )
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }
}