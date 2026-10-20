package com.neogym.application.port.in;

public interface ExcluirAgendamentoUseCase {

    void executar(
            Long usuarioId,
            String tipoUsuario,
            Long agendamentoId
    );
}