package com.neogym.infrastructure.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import javax.imageio.ImageIO;

@Service
public class CredencialArquivoStorageService {

    private static final long TAMANHO_MAXIMO = 5L * 1024 * 1024;

    private final Path diretorio;

    public CredencialArquivoStorageService(
            @Value("${neogym.upload-dir:./uploads/credenciais}")
            String uploadDir
    ) throws IOException {
        this.diretorio = Path.of(uploadDir)
                .toAbsolutePath()
                .normalize();

        Files.createDirectories(this.diretorio);
    }

    public String salvar(MultipartFile arquivo) throws IOException {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException(
                    "Envie uma imagem válida."
            );
        }

        if (arquivo.getSize() > TAMANHO_MAXIMO) {
            throw new IllegalArgumentException(
                    "A imagem deve ter no máximo 5 MB."
            );
        }

        String contentType = arquivo.getContentType();

        String extensao;
        if ("image/jpeg".equalsIgnoreCase(contentType)) {
            extensao = ".jpg";
        } else if ("image/png".equalsIgnoreCase(contentType)) {
            extensao = ".png";
        } else {
            throw new IllegalArgumentException(
                    "Formato inválido. Envie uma imagem JPG ou PNG."
            );
        }

        // Confere se o conteúdo pode ser decodificado como imagem.
        BufferedImage imagem = ImageIO.read(arquivo.getInputStream());

        if (imagem == null) {
            throw new IllegalArgumentException(
                    "O arquivo enviado não é uma imagem válida."
            );
        }

        String nomeArquivo = UUID.randomUUID() + extensao;
        Path destino = diretorio.resolve(nomeArquivo).normalize();

        if (!destino.startsWith(diretorio)) {
            throw new IllegalArgumentException("Nome de arquivo inválido.");
        }

        try (var input = arquivo.getInputStream()) {
            Files.copy(input, destino);
        }

        return nomeArquivo;
    }

    public Path obterCaminho(String nomeArquivo) {
        if (nomeArquivo == null
                || !nomeArquivo.matches(
                "^[a-fA-F0-9-]+\\.(jpg|png)$"
        )) {
            throw new IllegalArgumentException(
                    "Arquivo não encontrado."
            );
        }

        Path caminho = diretorio.resolve(nomeArquivo).normalize();

        if (!caminho.startsWith(diretorio) || !Files.isRegularFile(caminho)) {
            throw new IllegalArgumentException(
                    "Arquivo não encontrado."
            );
        }

        return caminho;
    }

    public void excluir(String nomeArquivo) throws IOException {
        if (nomeArquivo == null || nomeArquivo.isBlank()) {
            return;
        }

        Files.deleteIfExists(obterCaminho(nomeArquivo));
    }
}