package com.neogym.infrastructure.web.adapter;

import com.neogym.application.port.out.RequestMetadataPort;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@RequiredArgsConstructor
public class HttpRequestMetadataAdapter implements RequestMetadataPort {

    private final HttpServletRequest request;

    @Override
    public String getIpOrigem() {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].strip();
        }
        return request.getRemoteAddr();
    }

    @Override
    public String getUserAgent() {
        String ua = request.getHeader("User-Agent");
        if (ua == null) return null;
        return ua.length() > 255 ? ua.substring(0, 255) : ua;
    }
}
