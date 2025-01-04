package com.spartacoding.msa.order.infrastructure.messaging;

import com.spartacoding.msa.order.EventSerializer;
import com.spartacoding.msa.order.events.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class OrderProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    //@Retryable(
    //        value = {Exception.class}, // 재시도 대상 예외
    //        maxAttempts = 3,           // 최대 재시도 횟수
    //        backoff = @Backoff(delay = 2000) // 재시도 간격 (2초)
    //    )
    //    public OrderCreatedEvent send(String topic, OrderCreatedEvent event) {
    //        String eventToJson = EventSerializer.serialize(event);
    //
    //        // Kafka 메시지 전송
    //        kafkaTemplate.send(topic, eventToJson);
    //        log.info("Order Producer Sent order event: {}", eventToJson);
    //
    //        return event;
    //    }

    public OrderCreatedEvent send(String topic, OrderCreatedEvent event) {

        String eventToJson = EventSerializer.serialize(event);
        kafkaTemplate.send(topic, event.toString());
        log.info("Order Producer Sent order event: " + event);

        return event;
    }

    /**
     * 재시도 로직
     * */
    public void send2(String topic, OrderCreatedEvent event) {
        String eventToJson = EventSerializer.serialize(event);

        int maxRetries = 3;
        int retryDelay = 1000; // 1초

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                kafkaTemplate.send(topic, eventToJson).get(); // 동기 호출로 결과 확인
                log.info("Order Producer Sent order event: {}", eventToJson);
                return; // 성공하면 메서드 종료
            } catch (Exception e) {
                log.error("Attempt {} failed to send message to topic {}: {}", attempt, topic, e.getMessage());
                if (attempt == maxRetries) {
                    throw new RuntimeException("Failed to send message after retries", e);
                }
                try {
                    Thread.sleep(retryDelay); // 재시도 간격
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted", ie);
                }
            }
        }
    }
}
