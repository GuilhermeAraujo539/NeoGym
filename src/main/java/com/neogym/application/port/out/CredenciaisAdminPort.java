package com.neogym.application.port.out;

import com.neogym.application.dto.response.CredencialAdminResponse;

import java.util.List;

public interface CredenciaisAdminPort {

    List<CredencialAdminResponse> listar(
            String status,
            long offset,
            int tamanho
    );

    long contar(String status);

    ResultadoAvaliacao avaliar(
            long id,
            String status,
            String observacao
    );

    enum ResultadoAvaliacao {
        ATUALIZADA,
        NAO_ENCONTRADA,
        JA_AVALIADA
    }
}