package com.neogym.infrastructure.scheduler;

import com.neogym.application.port.out.RefreshTokenRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupScheduler {

    private final RefreshTokenRepositoryPort refreshTokenRepository;

    // Toda madrugada às 03:00
    @Scheduled(cron = "0 0 3 * * *")
    public void limparTokensExpirados() {
        log.info("Iniciando limpeza de refresh tokens expirados...");
        refreshTokenRepository.removerExpirados();
        log.info("Limpeza de refresh tokens concluída.");
    }
}
