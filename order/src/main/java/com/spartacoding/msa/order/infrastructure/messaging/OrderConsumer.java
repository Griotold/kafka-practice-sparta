package com.spartacoding.msa.order.infrastructure.messaging;

import com.spartacoding.msa.order.EventSerializer;
import com.spartacoding.msa.order.OrderTopic;
import com.spartacoding.msa.order.application.OrderApplicationService;
import com.spartacoding.msa.order.domain.Order;
import com.spartacoding.msa.order.domain.OrderRepository;
import com.spartacoding.msa.order.domain.OrderStatus;
import com.spartacoding.msa.order.events.OrderCompletedEvent;
import com.spartacoding.msa.order.events.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderRepository orderRepository;
    private final OrderProducer orderProducer;
//    private final OrderApplicationService orderApplicationService;

    /**
     * 결제 성공에 대한 이벤트 구독, 처리
     * */
    @Transactional
    @KafkaListener(topics = "payment-completed")
    public void listen(String kafkaMessage) {
        log.info("OrderConsumer.listen.kafkaMessage: {}", kafkaMessage);

        // 1. Order COMPLETED 로 업데이트
        PaymentSuccessEvent paymentSuccessEvent = EventSerializer.deserialize(kafkaMessage, PaymentSuccessEvent.class);

        Order order = orderRepository.findById(paymentSuccessEvent.id())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if ("COMPLETED".equals(paymentSuccessEvent.status())) {
            order.updateStatus(OrderStatus.COMPLETED);
        }

        // 2. "order-completed" 토픽에 이벤트 발행
        orderProducer.send(OrderTopic.ORDER_COMPLETED.getTopic(), OrderCompletedEvent.from(order));
    }
}
