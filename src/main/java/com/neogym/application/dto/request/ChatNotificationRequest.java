
package com.neogym.application.dto.request;

public record ChatNotificationRequest(
        String chatId,
        String messageId
) {
}
