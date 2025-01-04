package com.spartacoding.msa.payment.events;

import java.math.BigDecimal;

public record OrderCreatedEvent(
        Long orderId,
        String productId,
        Integer quantity,
        BigDecimal totalPrice,
        String customerEmail
) {
}
