package com.spartacoding.msa.order.events;

import com.spartacoding.msa.order.domain.Order;

import java.math.BigDecimal;

public record OrderCreatedEvent(
        Long orderId,
        String productId,
        Integer quantity,
        BigDecimal totalPrice,
        String customerEmail
) {
    public static OrderCreatedEvent from(Order order) {
        return new OrderCreatedEvent(
                order.getId(),
                order.getProductId(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getCustomerEmail()
        );
    }
}
