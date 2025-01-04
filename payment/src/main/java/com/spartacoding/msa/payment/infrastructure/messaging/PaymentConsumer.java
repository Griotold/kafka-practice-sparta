package com.spartacoding.msa.payment.infrastructure.messaging;

import com.spartacoding.msa.payment.EventSerializer;
import com.spartacoding.msa.payment.PaymentTopic;
import com.spartacoding.msa.payment.application.PaymentApplicationService;
import com.spartacoding.msa.payment.domain.Payment;
import com.spartacoding.msa.payment.domain.PaymentRepository;
import com.spartacoding.msa.payment.events.OrderCreatedEvent;
import com.spartacoding.msa.payment.events.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private final PaymentRepository paymentRepository;
    private final PaymentProducer paymentProducer;

    /**
     * 주문 생성에 대한 이벤트 구독, 처리
     * */
    @Transactional
    @KafkaListener(topics = "order-created")
    public void processPayment(String kafkaMessage) {
        log.info("processPayment.kafkaMessage =  {}", kafkaMessage);

        // 1. DB 저장
        OrderCreatedEvent orderCreatedEvent = EventSerializer.deserialize(kafkaMessage, OrderCreatedEvent.class);

        Payment payment = Payment.create(orderCreatedEvent.orderId(), orderCreatedEvent.totalPrice());

        paymentRepository.save(payment);

        PaymentSuccessEvent paymentSuccessEvent = PaymentSuccessEvent.from(payment);

        // 2. 결제 성공 이벤트 발행
        paymentProducer.send(PaymentTopic.PAYMENT_COMPLETED.getTopic(), paymentSuccessEvent);
    }
}
