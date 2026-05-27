package com.neogym.application.port.out;

import org.springframework.web.multipart.MultipartFile;

public interface FileStoragePort {

    /**
     * Persiste o arquivo e retorna a URL pública de acesso.
     * A implementação pode usar disco local, S3, Cloudinary, etc.
     */
    String salvar(MultipartFile arquivo, String subpasta);

    /**
     * Remove o arquivo pelo caminho/URL.
     */
    void remover(String url);
}
