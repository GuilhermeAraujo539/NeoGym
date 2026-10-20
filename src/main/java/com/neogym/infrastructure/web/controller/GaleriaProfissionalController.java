package com.neogym.infrastructure.web.controller;

import com.neogym.application.dto.response.FotoGaleriaProfissionalResponse;
import com.neogym.application.dto.response.FotoGaleriaProfissionalConteudoResponse;
import com.neogym.application.port.in.GerenciarGaleriaProfissionalUseCase;
import com.neogym.infrastructure.security.service.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GaleriaProfissionalController {

    private static final long MAX_BYTES = 5L * 1024 * 1024;

    private final GerenciarGaleriaProfissionalUseCase useCase;

    @GetMapping("/profissionais/{usuarioId}/fotos")
    public ResponseEntity<List<FotoGaleriaProfissionalResponse>> listar(
            @PathVariable Long usuarioId
    ) {
        return ResponseEntity.ok(useCase.listar(usuarioId));
    }

    @GetMapping("/profissionais/{usuarioId}/fotos/{fotoId}")
    public ResponseEntity<byte[]> obterFoto(
            @PathVariable Long usuarioId,
            @PathVariable Long fotoId
    ) {
        try {
            FotoGaleriaProfissionalConteudoResponse foto =
                    useCase.obterConteudo(usuarioId, fotoId);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(
                            foto.getContentType()
                    ))
                    .body(foto.getDados());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Foto não encontrada."
            );
        }
    }

    @PostMapping(
            value = "/personais/me/fotos",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<FotoGaleriaProfissionalResponse> adicionar(
            @RequestParam("foto") MultipartFile arquivo,
            Authentication authentication
    ) {
        AuthenticatedUser usuario = exigirPersonal(authentication);

        if (arquivo.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selecione uma imagem."
            );
        }

        if (arquivo.getSize() > MAX_BYTES) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A imagem deve ter no máximo 5 MB."
            );
        }

        String contentType = determinarContentType(arquivo);

        try {
            var resultado = useCase.adicionar(
                    usuario.getUsuarioId(),
                    arquivo.getBytes(),
                    contentType
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(resultado);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    e.getMessage()
            );
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Não foi possível ler a imagem.",
                    e
            );
        }
    }

    @DeleteMapping("/personais/me/fotos/{fotoId}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long fotoId,
            Authentication authentication
    ) {
        AuthenticatedUser usuario = exigirPersonal(authentication);

        try {
            useCase.excluir(usuario.getUsuarioId(), fotoId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Foto não encontrada."
            );
        }
    }

    private AuthenticatedUser exigirPersonal(
            Authentication authentication
    ) {
        boolean ehPersonal = authentication != null
                && authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_PERSONAL"::equals);

        if (!ehPersonal) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Apenas o personal pode gerenciar a própria galeria."
            );
        }

        return (AuthenticatedUser) authentication.getPrincipal();
    }

    private String determinarContentType(MultipartFile arquivo) {
        String contentType = arquivo.getContentType();

        if (contentType != null &&
                !contentType.equalsIgnoreCase(
                        MediaType.APPLICATION_OCTET_STREAM_VALUE
                )) {
            return contentType.toLowerCase(Locale.ROOT);
        }

        String nome = arquivo.getOriginalFilename();

        if (nome != null) {
            String nomeMinusculo = nome.toLowerCase(Locale.ROOT);

            if (nomeMinusculo.endsWith(".jpg") ||
                    nomeMinusculo.endsWith(".jpeg")) {
                return "image/jpeg";
            }

            if (nomeMinusculo.endsWith(".png")) {
                return "image/png";
            }

            if (nomeMinusculo.endsWith(".webp")) {
                return "image/webp";
            }
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Formato inválido. Utilize JPEG, PNG ou WebP."
        );
    }
}