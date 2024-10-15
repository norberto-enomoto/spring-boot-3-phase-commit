package com.example.notification.dto;

import lombok.Data;

@Data
public class TransactionInfo {
    private String transactionId;
    private String ticketId;
    private Double amount;
}
