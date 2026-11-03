package com.neogym.domain.entity;

import java.time.LocalDateTime;

public class FotoGaleriaProfissional {

    private Long id;
    private Long usuarioId;
    private byte[] dados;
    private String contentType;
    private LocalDateTime criadoEm;

    public FotoGaleriaProfissional(
            Long id,
            Long usuarioId,
            byte[] dados,
            String contentType,
            LocalDateTime criadoEm
    ) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.dados = dados;
        this.contentType = contentType;
        this.criadoEm = criadoEm;
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public byte[] getDados() {
        return dados;
    }

    public String getContentType() {
        return contentType;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}