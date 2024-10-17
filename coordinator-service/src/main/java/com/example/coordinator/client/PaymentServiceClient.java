package com.example.coordinator.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "paymentService", url = "${payment.service.url}")
public interface PaymentServiceClient extends ParticipantClient {
}
