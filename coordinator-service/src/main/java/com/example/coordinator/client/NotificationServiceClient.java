package com.example.coordinator.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "notificationService", url = "${service.notification.url}")
public interface NotificationServiceClient extends ParticipantClient {
}