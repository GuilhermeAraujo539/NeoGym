package com.neogym.application.port.in;

import com.neogym.application.dto.request.LoginRequest;
import com.neogym.application.dto.response.AuthResponse;

public interface LoginUseCase {
    AuthResponse executar(LoginRequest request);
}
