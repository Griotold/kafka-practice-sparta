package com.spartacoding.msa.order.events;

import com.spartacoding.msa.order.domain.Order;

import java.math.BigDecimal;

public record OrderCompletedEvent(
        Long orderId,
        String productId,
        Integer quantity,
        BigDecimal totalPrice,
        String customerEmail,
        String status
) {
    public static OrderCompletedEvent from(Order order) {
        return new OrderCompletedEvent(
                order.getId(),
                order.getProductId(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getCustomerEmail(),
                order.getStatus().name()
        );
    }
}

