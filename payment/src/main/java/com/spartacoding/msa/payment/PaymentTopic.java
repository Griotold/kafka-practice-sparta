package com.spartacoding.msa.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 토픽 이름이 많아지면 enum 으로 관리한다.
 * */
@Getter
@AllArgsConstructor
public enum PaymentTopic {
    // 주문 생성 이벤트 (Order MSA가 발행)
    ORDER_CREATED("order-created"),

    // 결제 완료 이벤트 (Payment MSA가 발행)
    PAYMENT_COMPLETED("payment-completed")
    ;

    private final String topic;

}
