package com.neogym.application.usecase;

import com.neogym.application.dto.response.ProfissionalProximoResponse;
import com.neogym.application.port.in.BuscarProfissionaisProximosUseCase;
import com.neogym.infrastructure.projection.ProfissionalProximoProjection;
import com.neogym.application.port.out.ProfissionalRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarProfissionaisProximosService
        implements BuscarProfissionaisProximosUseCase {

    private final ProfissionalRepositoryPort repository;

    @Override
    public Page<ProfissionalProximoResponse> executar(
            Double latitude,
            Double longitude,
            Double raioKm,
            int pagina,
            int tamanho
    ) {

        Pageable pageable = PageRequest.of(pagina, tamanho);

        return repository
                .buscarPersonalsProximos(
                        latitude,
                        longitude,
                        raioKm,
                        pageable
                )
                .map(this::toResponse);
    }

    private ProfissionalProximoResponse toResponse(
            ProfissionalProximoProjection profissional
    ) {
        return ProfissionalProximoResponse.builder()
                .id(profissional.getId())
                .usuarioId(profissional.getUsuarioId())
                .nome(profissional.getNome())
                .email(profissional.getEmail())
                .tipo(profissional.getTipo())
                .distanciaKm(
                        Math.round(profissional.getDistanciaKm() * 100.0) / 100.0
                )
                .especialidade(profissional.getEspecialidade())
                .apresentacao(profissional.getApresentacao())
                .areasAtuacao(profissional.getAreasAtuacao())
                .horariosDisponiveis(profissional.getHorariosDisponiveis())
                .build();
    }
}
