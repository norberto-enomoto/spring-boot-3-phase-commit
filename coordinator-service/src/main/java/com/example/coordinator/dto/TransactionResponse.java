package com.example.coordinator.dto;

import lombok.Data;

@Data
public class TransactionResponse {
    private String transactionId;
    private String status;
    private String message;
}