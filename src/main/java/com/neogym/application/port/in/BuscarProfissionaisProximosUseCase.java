package com.neogym.application.port.in;

import com.neogym.application.dto.response.ProfissionalProximoResponse;
import org.springframework.data.domain.Page;

public interface BuscarProfissionaisProximosUseCase {

    Page<ProfissionalProximoResponse> executar(
            Double latitude,
            Double longitude,
            Double raioKm,
            int pagina,
            int tamanho
    );
}
