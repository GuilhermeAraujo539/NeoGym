package com.neogym.application.port.in;

import com.neogym.application.dto.request.RefreshTokenRequest;

public interface LogoutUseCase {
    void executar(RefreshTokenRequest request);
}
