package com.neogym.application.port.in;

import com.neogym.application.dto.response.CredencialAdminResponse;
import com.neogym.application.dto.response.CredenciaisAdminPaginaResponse;
import com.neogym.application.port.out.CredenciaisAdminPort;

public class GerenciarCredenciaisAdminUseCase {

    private final CredenciaisAdminPort credenciaisAdminPort;

    public GerenciarCredenciaisAdminUseCase(
            CredenciaisAdminPort credenciaisAdminPort
    ) {
        this.credenciaisAdminPort = credenciaisAdminPort;
    }

    public CredenciaisAdminPaginaResponse listar(
            String status,
            int pagina,
            int tamanho
    ) {
        String filtro = status == null || status.isBlank()
                ? null
                : status;

        long offset = (long) pagina * tamanho;

        var conteudo = credenciaisAdminPort.listar(
                filtro,
                offset,
                tamanho
        );

        long total = credenciaisAdminPort.contar(filtro);

        return new CredenciaisAdminPaginaResponse(
                conteudo,
                total
        );
    }

    public CredenciaisAdminPort.ResultadoAvaliacao avaliar(
            long id,
            String status,
            String observacao
    ) {
        return credenciaisAdminPort.avaliar(
                id,
                status,
                observacao
        );
    }
}
