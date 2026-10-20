package com.neogym.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FotoGaleriaProfissionalConteudoResponse {
    private byte[] dados;
    private String contentType;
}