package com.neogym.application.port.out;

import com.neogym.application.dto.response.AdminUsuarioResponse;

import java.util.List;

public interface AdminUsuariosPort {

    List<AdminUsuarioResponse> listar(
            String tipo,
            Boolean ativo,
            long offset,
            int tamanho
    );

    long contar(String tipo, Boolean ativo);

    ResultadoAtualizacao atualizar(
            long id,
            String nome,
            String email,
            Boolean ativo
    );

    ResultadoExclusao excluir(long id);

    enum ResultadoAtualizacao {
        ATUALIZADO,
        NAO_ENCONTRADO,
        EMAIL_EM_USO,
        ADMIN_PROTEGIDO,
        NENHUMA_ALTERACAO
    }

    enum ResultadoExclusao {
        EXCLUIDO,
        NAO_ENCONTRADO,
        ADMIN_PROTEGIDO,
        POSSUI_VINCULOS
    }
}