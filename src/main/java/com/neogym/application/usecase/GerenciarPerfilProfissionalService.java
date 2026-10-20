package com.neogym.application.usecase;

import com.neogym.application.dto.request.AtualizarPerfilProfissionalRequest;
import com.neogym.application.dto.response.PerfilProfissionalResponse;
import com.neogym.application.port.in.GerenciarPerfilProfissionalUseCase;
import com.neogym.application.port.out.PersonalRepositoryPort;
import com.neogym.domain.entity.Personal;
import lombok.RequiredArgsConstructor;

public class GerenciarPerfilProfissionalService
        implements GerenciarPerfilProfissionalUseCase {

    private final PersonalRepositoryPort personalRepository;

    public GerenciarPerfilProfissionalService(
            PersonalRepositoryPort personalRepository
    ) {
        this.personalRepository = personalRepository;
    }

    @Override
    public PerfilProfissionalResponse buscar(Long usuarioId) {
        Personal personal = buscarPersonal(usuarioId);
        return toResponse(personal);
    }

    @Override
    public PerfilProfissionalResponse atualizar(
            Long usuarioId,
            AtualizarPerfilProfissionalRequest request
    ) {
        Personal atual = buscarPersonal(usuarioId);

        Personal atualizado = Personal.builder()
                .id(atual.getId())
                .usuarioId(atual.getUsuarioId())

                // Dados cadastrais e aprovação são preservados.
                .cref(atual.getCref())
                .estadoCref(atual.getEstadoCref())
                .statusCref(atual.getStatusCref())

                .especialidade(normalizar(request.getEspecialidade()))
                .apresentacao(normalizar(request.getApresentacao()))
                .areasAtuacao(normalizar(request.getAreasAtuacao()))
                .horariosDisponiveis(
                        normalizar(request.getHorariosDisponiveis())
                )
                .build();

        return toResponse(personalRepository.salvar(atualizado));
    }

    private Personal buscarPersonal(Long usuarioId) {
        return personalRepository.buscarPorUsuarioId(usuarioId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Perfil profissional não encontrado"
                        )
                );
    }

    private String normalizar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private PerfilProfissionalResponse toResponse(Personal personal) {
        return PerfilProfissionalResponse.builder()
                .id(personal.getId())
                .usuarioId(personal.getUsuarioId())
                .cref(personal.getCref())
                .estadoCref(personal.getEstadoCref())
                .statusCref(personal.getStatusCref())
                .especialidade(personal.getEspecialidade())
                .apresentacao(personal.getApresentacao())
                .areasAtuacao(personal.getAreasAtuacao())
                .horariosDisponiveis(
                        personal.getHorariosDisponiveis()
                )
                .build();
    }
}