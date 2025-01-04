package com.spartacoding.msa.payment.infrastructure.messaging;

import com.spartacoding.msa.payment.EventSerializer;
import com.spartacoding.msa.payment.events.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, PaymentSuccessEvent event) {
        String eventToJson = EventSerializer.serialize(event);
        kafkaTemplate.send(topic, eventToJson);
        log.info("PaymentProducer Sent PaymentSuccessEvent: " + eventToJson);
    }
}
