package com.spartacoding.msa.notification.infrastructure.messaging;

import com.spartacoding.msa.notification.EventSerializer;
import com.spartacoding.msa.notification.events.OrderCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    /**
     * 주문 완료에 대한 이벤트를 구독, 처리
     * 주문 완료에 대한 알림 메시지는 로그로 출력
     * */

    @KafkaListener(topics = "order-completed")
    public void listen(String kafkaMessage) {
        log.info("NotificationConsumer.listen.kafkaMessage: {}", kafkaMessage);

        OrderCompletedEvent orderCompletedEvent = EventSerializer.deserialize(kafkaMessage, OrderCompletedEvent.class);

        log.info("NotificationConsumer.listen.orderCompletedEvent: {}", orderCompletedEvent);
    }
}
