package com.example.coordinator.dto;

import lombok.Data;

@Data
public class TransactionRequest {
    private String ticketId;
    private Double amount;
}
