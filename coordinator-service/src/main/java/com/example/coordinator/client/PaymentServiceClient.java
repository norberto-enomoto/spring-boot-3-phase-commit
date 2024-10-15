package com.example.coordinator.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "paymentService", url = "${service.payment.url}")
public interface PaymentServiceClient extends ParticipantClient {
}
