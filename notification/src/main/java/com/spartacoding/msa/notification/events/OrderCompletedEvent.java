package com.spartacoding.msa.notification.events;

import java.math.BigDecimal;

public record OrderCompletedEvent(
        Long orderId,
        String productId,
        Integer quantity,
        BigDecimal totalPrice,
        String customerEmail,
        String status
) {
}
