package com.spartacoding.msa.order.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String customerEmail;
    private BigDecimal totalPrice;

    public static Order create(String productId, int quantity, OrderStatus status, String customerEmail, BigDecimal totalPrice) {
        return Order.builder()
                .productId(productId)
                .quantity(quantity)
                .status(status)
                .customerEmail(customerEmail)
                .totalPrice(totalPrice)
                .build();
    }

    public void updateStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }
}
