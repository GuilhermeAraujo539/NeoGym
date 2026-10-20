package com.neogym.infrastructure.persistence.adapter;

import com.neogym.application.port.out.AgendamentoRepositoryPort;
import com.neogym.domain.entity.Agendamento;
import com.neogym.domain.enums.TipoProfissionalAgendamento;
import com.neogym.infrastructure.persistence.entity.AgendamentoJpaEntity;
import com.neogym.infrastructure.persistence.repository.AgendamentoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AgendamentoRepositoryAdapter
        implements AgendamentoRepositoryPort {

    private final AgendamentoJpaRepository repository;

    @Override
    public Agendamento salvar(Agendamento agendamento) {

        AgendamentoJpaEntity entity =
                new AgendamentoJpaEntity(
                        agendamento.getAlunoId(),
                        agendamento.getPersonalId(),
                        agendamento.getNutricionistaId(),
                        agendamento.getTitulo(),
                        agendamento.getDescricao(),
                        agendamento.getDataHora(),
                        agendamento.getStatus(),
                        agendamento.getCriadoEm()
                );

        return toDomain(repository.save(entity));
    }

    @Override
    public Optional<Agendamento> buscarPorId(Long id) {
        return repository
                .findById(id)
                .map(this::toDomain);
    }

    @Override
    public void excluir(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Agendamento> buscarPorAlunoId(Long alunoId) {
        return repository
                .findByAlunoIdOrderByDataHoraAsc(alunoId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Agendamento> buscarPorPersonalId(Long personalId) {
        return repository
                .findByPersonalIdOrderByDataHoraAsc(personalId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Agendamento> buscarPorNutricionistaId(
            Long nutricionistaId
    ) {
        return repository
                .findByNutricionistaIdOrderByDataHoraAsc(
                        nutricionistaId
                )
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private Agendamento toDomain(AgendamentoJpaEntity entity) {

        TipoProfissionalAgendamento tipo;

        if (entity.getPersonalId() != null) {
            tipo = TipoProfissionalAgendamento.PERSONAL;
        } else {
            tipo = TipoProfissionalAgendamento.NUTRICIONISTA;
        }

        return new Agendamento(
                entity.getId(),
                entity.getAlunoId(),
                entity.getPersonalId(),
                entity.getNutricionistaId(),
                entity.getTitulo(),
                entity.getDescricao(),
                entity.getDataHora(),
                entity.getStatus(),
                entity.getCriadoEm(),
                tipo
        );
    }
}