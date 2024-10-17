package com.example.coordinator.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "ticketService", url = "${ticket.service.url}")
public interface TicketServiceClient extends ParticipantClient {
}