package com.spartacoding.msa.payment.events;

import com.spartacoding.msa.payment.domain.Payment;

import java.math.BigDecimal;

public record PaymentSuccessEvent(
        Long id,
        Long orderId,
        BigDecimal amount,
        String status
) {

    public static PaymentSuccessEvent from(Payment payment) {
        return new PaymentSuccessEvent(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getStatus().name()
        );
    }
}
