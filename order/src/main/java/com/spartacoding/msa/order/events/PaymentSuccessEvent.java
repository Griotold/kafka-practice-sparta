package com.spartacoding.msa.order.events;

import java.math.BigDecimal;

public record PaymentSuccessEvent(
        Long id,
        Long orderId,
        BigDecimal amount,
        String status
) {
}
