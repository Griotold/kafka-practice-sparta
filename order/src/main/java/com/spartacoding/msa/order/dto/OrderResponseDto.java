package com.spartacoding.msa.order.dto;

import com.spartacoding.msa.order.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

public record OrderResponseDto(
         Long orderId,
         String productId,
         int quantity,
         BigDecimal totalPrice,
         String status,
         String customerEmail
         )
{
        public static OrderResponseDto from(Order order){
            return new OrderResponseDto(
                    order.getId(),
                    order.getProductId(),
                    order.getQuantity(),
                    order.getTotalPrice(),
                    order.getStatus().name(),
                    order.getCustomerEmail()
            );
        }
}
