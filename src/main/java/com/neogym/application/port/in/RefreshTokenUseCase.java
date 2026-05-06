package com.neogym.application.port.in;

import com.neogym.application.dto.request.RefreshTokenRequest;
import com.neogym.application.dto.response.AuthResponse;

public interface RefreshTokenUseCase {
    AuthResponse executar(RefreshTokenRequest request);
}
