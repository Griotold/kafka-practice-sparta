package com.spartacoding.msa.payment.infrastructure.messaging;

import com.spartacoding.msa.payment.EventSerializer;
import com.spartacoding.msa.payment.events.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PaymentProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, PaymentSuccessEvent event) {
        String eventToJson = EventSerializer.serialize(event);
        kafkaTemplate.send(topic, eventToJson);
        log.info("Payment Producer Sent payment event: " + eventToJson);
    }
}
