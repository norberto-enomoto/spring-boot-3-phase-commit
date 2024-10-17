package com.example.coordinator.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "notificationService", url = "${notification.service.url}")
public interface NotificationServiceClient extends ParticipantClient {
}