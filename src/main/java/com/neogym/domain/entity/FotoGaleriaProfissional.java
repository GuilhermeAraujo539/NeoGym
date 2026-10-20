package com.neogym.domain.entity;

import java.time.LocalDateTime;

public class FotoGaleriaProfissional {

    private final Long id;
    private final Long personalId;
    private final byte[] dados;
    private final String contentType;
    private final LocalDateTime criadoEm;

    public FotoGaleriaProfissional(
            Long id,
            Long personalId,
            byte[] dados,
            String contentType,
            LocalDateTime criadoEm
    ) {
        this.id = id;
        this.personalId = personalId;
        this.dados = dados;
        this.contentType = contentType;
        this.criadoEm = criadoEm;
    }

    public Long getId() {
        return id;
    }

    public Long getPersonalId() {
        return personalId;
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