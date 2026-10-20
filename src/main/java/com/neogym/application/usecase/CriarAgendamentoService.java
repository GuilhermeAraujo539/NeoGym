package com.neogym.application.usecase;

import com.neogym.application.dto.request.CriarAgendamentoRequest;
import com.neogym.application.dto.response.AgendamentoResponse;
import com.neogym.application.port.in.CriarAgendamentoUseCase;
import com.neogym.application.port.out.*;
import com.neogym.domain.entity.Agendamento;
import com.neogym.domain.enums.TipoProfissionalAgendamento;

public class CriarAgendamentoService
        implements CriarAgendamentoUseCase {

    private final AlunoRepositoryPort alunoRepository;
    private final PersonalRepositoryPort personalRepository;
    private final NutricionistaRepositoryPort nutricionistaRepository;
    private final PersonalAlunoRepositoryPort personalAlunoRepository;
    private final AgendamentoRepositoryPort agendamentoRepository;
    private final UsuarioRepositoryPort usuarioRepository;

    public CriarAgendamentoService(
            AlunoRepositoryPort alunoRepository,
            PersonalRepositoryPort personalRepository,
            NutricionistaRepositoryPort nutricionistaRepository,
            PersonalAlunoRepositoryPort personalAlunoRepository,
            AgendamentoRepositoryPort agendamentoRepository,
            UsuarioRepositoryPort usuarioRepository
    ) {
        this.alunoRepository = alunoRepository;
        this.personalRepository = personalRepository;
        this.nutricionistaRepository = nutricionistaRepository;
        this.personalAlunoRepository = personalAlunoRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public AgendamentoResponse executar(
            Long usuarioId,
            String tipoUsuario,
            CriarAgendamentoRequest request
    ) {

        if ("ROLE_ALUNO".equals(tipoUsuario)) {
            return criarComoAluno(
                    usuarioId,
                    request
            );
        }

        if ("ROLE_PERSONAL".equals(tipoUsuario)) {
            return criarComoPersonal(
                    usuarioId,
                    request
            );
        }

        if ("ROLE_NUTRICIONISTA".equals(tipoUsuario)) {
            return criarComoNutricionista(
                    usuarioId,
                    request
            );
        }

        throw new RuntimeException(
                "Usuário não possui permissão para criar agendamento"
        );
    }

    private AgendamentoResponse criarComoAluno(
            Long usuarioId,
            CriarAgendamentoRequest request
    ) {

        var aluno = alunoRepository
                .buscarPorUsuarioId(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Aluno não encontrado"
                        )
                );

        if (request.getProfissionalId() == null ||
                request.getTipoProfissional() == null) {

            throw new RuntimeException(
                    "Profissional deve ser informado"
            );
        }

        Long personalId = null;
        Long nutricionistaId = null;

        if (request.getTipoProfissional()
                == TipoProfissionalAgendamento.PERSONAL) {

            var personal = personalRepository
                    .buscarPorId(request.getProfissionalId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Personal não encontrado"
                            )
                    );

            if (!personal.isAprovado()) {
                throw new RuntimeException(
                        "Personal não está aprovado"
                );
            }

            personalId = personal.getId();

        } else {

            var nutricionista = nutricionistaRepository
                    .buscarPorId(
                            request.getProfissionalId()
                    )
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Nutricionista não encontrado"
                            )
                    );

            if (!nutricionista.isAprovado()) {
                throw new RuntimeException(
                        "Nutricionista não está aprovado"
                );
            }

            nutricionistaId =
                    nutricionista.getId();
        }

        Agendamento agendamento =
                Agendamento.criar(
                        aluno.getId(),
                        personalId,
                        nutricionistaId,
                        request.getTitulo(),
                        request.getDescricao(),
                        request.getDataHora(),
                        request.getTipoProfissional()
                );

        return toResponse(
                agendamentoRepository.salvar(
                        agendamento
                )
        );
    }

    private AgendamentoResponse criarComoPersonal(
            Long usuarioId,
            CriarAgendamentoRequest request
    ) {

        if (request.getAlunoId() == null) {
            throw new RuntimeException(
                    "Aluno deve ser informado"
            );
        }

        var personal = personalRepository
                .buscarPorUsuarioId(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Personal não encontrado"
                        )
                );

        if (!personal.isAprovado()) {
            throw new RuntimeException(
                    "Personal não está aprovado"
            );
        }

        alunoRepository
                .buscarPorId(request.getAlunoId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Aluno não encontrado"
                        )
                );

        boolean possuiVinculo =
                personalAlunoRepository.existeVinculo(
                        personal.getId(),
                        request.getAlunoId()
                );

        if (!possuiVinculo) {
            throw new RuntimeException(
                    "Personal não possui vínculo com este aluno"
            );
        }

        Agendamento agendamento =
                Agendamento.criar(
                        request.getAlunoId(),
                        personal.getId(),
                        null,
                        request.getTitulo(),
                        request.getDescricao(),
                        request.getDataHora(),
                        TipoProfissionalAgendamento.PERSONAL
                );

        return toResponse(
                agendamentoRepository.salvar(
                        agendamento
                )
        );
    }

    private AgendamentoResponse criarComoNutricionista(
            Long usuarioId,
            CriarAgendamentoRequest request
    ) {

        if (request.getAlunoId() == null) {
            throw new RuntimeException(
                    "Aluno deve ser informado"
            );
        }

        var nutricionista =
                nutricionistaRepository
                        .buscarPorUsuarioId(usuarioId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Nutricionista não encontrado"
                                )
                        );

        if (!nutricionista.isAprovado()) {
            throw new RuntimeException(
                    "Nutricionista não está aprovado"
            );
        }

        alunoRepository
                .buscarPorId(request.getAlunoId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Aluno não encontrado"
                        )
                );

        Agendamento agendamento =
                Agendamento.criar(
                        request.getAlunoId(),
                        null,
                        nutricionista.getId(),
                        request.getTitulo(),
                        request.getDescricao(),
                        request.getDataHora(),
                        TipoProfissionalAgendamento.NUTRICIONISTA
                );

        return toResponse(
                agendamentoRepository.salvar(
                        agendamento
                )
        );
    }

    private AgendamentoResponse toResponse(
            Agendamento agendamento
    ) {

        Long profissionalId;
        String profissionalNome;

        if (agendamento.getTipoProfissional()
                == TipoProfissionalAgendamento.PERSONAL) {

            profissionalId = agendamento.getPersonalId();

            var personal = personalRepository
                    .buscarPorId(profissionalId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Personal não encontrado"
                            )
                    );

            profissionalNome = usuarioRepository
                    .buscarPorId(personal.getUsuarioId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Usuário do Personal não encontrado"
                            )
                    )
                    .getNome();

        } else {

            profissionalId = agendamento.getNutricionistaId();

            var nutricionista = nutricionistaRepository
                    .buscarPorId(profissionalId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Nutricionista não encontrado"
                            )
                    );

            profissionalNome = usuarioRepository
                    .buscarPorId(nutricionista.getUsuarioId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Usuário da Nutricionista não encontrado"
                            )
                    )
                    .getNome();
        }

        return AgendamentoResponse.builder()
                .id(agendamento.getId())
                .alunoId(agendamento.getAlunoId())
                .profissionalId(profissionalId)
                .profissionalNome(profissionalNome)
                .tipoProfissional(
                        agendamento.getTipoProfissional()
                )
                .titulo(agendamento.getTitulo())
                .descricao(agendamento.getDescricao())
                .dataHora(agendamento.getDataHora())
                .status(agendamento.getStatus())
                .criadoEm(agendamento.getCriadoEm())
                .build();
    }
}