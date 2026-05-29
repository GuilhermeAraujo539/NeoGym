package com.neogym.application.port.out;

import org.springframework.web.multipart.MultipartFile;

public interface FileStoragePort {

    String salvar(MultipartFile arquivo, String subpasta);

    void remover(String url);
}
