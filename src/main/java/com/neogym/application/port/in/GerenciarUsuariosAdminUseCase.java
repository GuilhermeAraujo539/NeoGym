package com.neogym.application.port.in;

import com.neogym.application.dto.response.AdminUsuarioResponse;
import com.neogym.application.dto.response.AdminUsuariosPaginaResponse;
import com.neogym.application.port.out.AdminUsuariosPort;

public class GerenciarUsuariosAdminUseCase {

    private final AdminUsuariosPort adminUsuariosPort;

    public GerenciarUsuariosAdminUseCase(
            AdminUsuariosPort adminUsuariosPort
    ) {
        this.adminUsuariosPort = adminUsuariosPort;
    }

    public AdminUsuariosPaginaResponse listar(
            String tipo,
            Boolean ativo,
            int pagina,
            int tamanho
    ) {
        long offset = (long) pagina * tamanho;

        var conteudo = adminUsuariosPort.listar(
                tipo,
                ativo,
                offset,
                tamanho
        );

        long total = adminUsuariosPort.contar(tipo, ativo);

        return new AdminUsuariosPaginaResponse(conteudo, total);
    }

    public AdminUsuariosPort.ResultadoAtualizacao atualizar(
            long id,
            String nome,
            String email,
            Boolean ativo
    ) {
        return adminUsuariosPort.atualizar(id, nome, email, ativo);
    }

    public AdminUsuariosPort.ResultadoExclusao excluir(long id) {
        return adminUsuariosPort.excluir(id);
    }
}