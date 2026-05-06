package com.neogym.infrastructure.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "neogym.jwt")
public class JwtProperties {
    private String secret;
    private long   accessTokenExpirationMs;
    private long   refreshTokenExpirationMs;
}
