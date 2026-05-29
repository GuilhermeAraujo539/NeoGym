package com.neogym.infrastructure.storage;

import com.neogym.application.port.out.FileStoragePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


@Slf4j
@Component
public class LocalFileStorageAdapter implements FileStoragePort {

    @Value("${neogym.storage.base-path:uploads}")
    private String basePath;

    @Value("${neogym.storage.base-url:http://localhost:8080/arquivos}")
    private String baseUrl;

    @Override
    public String salvar(MultipartFile arquivo, String subpasta) {
        try {
            String extensao = resolverExtensao(arquivo.getContentType());
            String nomeArquivo = UUID.randomUUID() + extensao;

            Path destino = Paths.get(basePath, subpasta).toAbsolutePath().normalize();
            Files.createDirectories(destino);

            Path caminhoArquivo = destino.resolve(nomeArquivo);
            Files.copy(arquivo.getInputStream(), caminhoArquivo, StandardCopyOption.REPLACE_EXISTING);

            String url = baseUrl + "/" + subpasta + "/" + nomeArquivo;
            log.info("Arquivo salvo: {}", url);
            return url;

        } catch (IOException e) {
            log.error("Erro ao salvar arquivo: {}", e.getMessage());
            throw new RuntimeException("Falha ao salvar arquivo. Tente novamente.", e);
        }
    }

    @Override
    public void remover(String url) {
        try {
            String caminho = url.replace(baseUrl, basePath);
            Path path = Paths.get(caminho).toAbsolutePath().normalize();
            if (!path.startsWith(Paths.get(basePath).toAbsolutePath())) {
                log.warn("Tentativa de remover arquivo fora do diretório base: {}", url);
                return;
            }
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.warn("Erro ao remover arquivo {}: {}", url, e.getMessage());
        }
    }

    private String resolverExtensao(String contentType) {
        if (contentType == null) return ".bin";
        return switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png"  -> ".png";
            case "application/pdf" -> ".pdf";
            default -> ".bin";
        };
    }
}
