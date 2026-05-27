package com.neogym.application.port.out;

import com.neogym.domain.enums.TipoUsuario;

public interface AdminRepositoryPort {

    long contarUsuariosPorTipo(TipoUsuario tipo);

    long contarTotalUsuarios();

    long contarUsuariosPorAtivo(boolean ativo);

    long contarVinculosPendentes();
}
