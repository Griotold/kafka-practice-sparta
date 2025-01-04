package com.spartacoding.msa.payment.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public static Payment create(final Long orderId, final BigDecimal amount, final PaymentStatus status) {
        return Payment.builder()
                .orderId(orderId)
                .amount(amount)
                .status(status)
                .build();
    }

    public static Payment create(final Long orderId, final BigDecimal amount) {
        return create(orderId, amount, PaymentStatus.COMPLETED);
    }
}

