package com.neogym.application.port.out;

import com.neogym.domain.entity.Agendamento;

import java.util.*;

public interface AgendamentoRepositoryPort {

    Agendamento salvar(Agendamento agendamento);

    List<Agendamento> buscarPorAlunoId(Long alunoId);

    List<Agendamento> buscarPorPersonalId(Long personalId);

    List<Agendamento> buscarPorNutricionistaId(Long nutricionistaId);

    Optional<Agendamento> buscarPorId(Long id);

    void excluir(Long id);
}