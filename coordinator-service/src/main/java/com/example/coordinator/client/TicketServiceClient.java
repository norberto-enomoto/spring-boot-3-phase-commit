package com.example.coordinator.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "ticketService", url = "${service.ticket.url}")
public interface TicketServiceClient extends ParticipantClient {
}