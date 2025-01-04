package com.spartacoding.msa.order.application;

import com.spartacoding.msa.order.OrderTopic;
import com.spartacoding.msa.order.domain.Order;
import com.spartacoding.msa.order.domain.OrderRepository;
import com.spartacoding.msa.order.domain.OrderStatus;
import com.spartacoding.msa.order.dto.OrderCreateDto;
import com.spartacoding.msa.order.dto.OrderResponseDto;
import com.spartacoding.msa.order.events.OrderCreatedEvent;
import com.spartacoding.msa.order.infrastructure.messaging.OrderProducer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class OrderApplicationService {
    private final OrderRepository orderRepository;
    private final OrderProducer orderProducer;

    @Transactional
    public OrderResponseDto createOrder(OrderCreateDto orderCreateDto) {
        log.info("createOrder.OrderCreateDto = {}", orderCreateDto);
        // 1. DB 저장
        Order order = Order.create(
                orderCreateDto.getProductId(),
                orderCreateDto.getQuantity(),
                OrderStatus.CREATED,
                orderCreateDto.getCustomerEmail(),
                orderCreateDto.getTotalPrice());
        orderRepository.save(order);

        // 2. 카프카 발행
        OrderCreatedEvent event = OrderCreatedEvent.from(order);
        log.info("createOrder.OrderCreatedEvent = {}", event);
        orderProducer.send(OrderTopic.ORDER_CREATED.getTopic(), event);
        return OrderResponseDto.from(order);
    }

    public void completeOrder() {
        // TODO 주문이 성공적으로 완료된 로직과, 이벤트를 발행해 주세요
    }

    public List<OrderResponseDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private OrderResponseDto convertToDto(Order order) {
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
