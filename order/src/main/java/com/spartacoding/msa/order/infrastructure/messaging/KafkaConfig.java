package com.spartacoding.msa.order.infrastructure.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

/**
 * Kafka의 DefaultErrorHandler를 통해 재시도 로직 구현 가능
 * 문제는 consumer만 해당 producer는 직접 구현하거나 Spring Retry 적용해야 함
 * */
@Configuration
public class KafkaConfig {
    @Bean
    public DefaultErrorHandler errorHandler() {
        FixedBackOff fixedBackOff = new FixedBackOff(1000L, 3);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(fixedBackOff);

        errorHandler.setRetryListeners((record, exception, deliveryAttempt) ->
                System.out.printf("Failed record in retry listener: %s, attempt: %d%n", record.value(), deliveryAttempt)
        );

        return errorHandler;
    }
}
