package com.example.coordinator.dto;

import lombok.Data;

@Data
public class TransactionInfo {
    private String transactionId;
    private String ticketId;
    private Double amount;
}