package com.neogym.infrastructure.web.controller;

import com.neogym.application.dto.response.ProfissionalProximoResponse;
import com.neogym.application.port.in.BuscarProfissionaisProximosUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profissionais")
@RequiredArgsConstructor
public class ProfissionalController {

    private final BuscarProfissionaisProximosUseCase buscarProfissionaisProximosUseCase;

    @GetMapping("/proximos")
    public Page<ProfissionalProximoResponse> buscarProximos(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5") Double raioKm,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanho
    ) {
        return buscarProfissionaisProximosUseCase.executar(
                latitude,
                longitude,
                raioKm,
                pagina,
                tamanho
        );
    }
}