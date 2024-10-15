package com.example.coordinator.controller;

import com.example.coordinator.dto.TransactionRequest;
import com.example.coordinator.dto.TransactionResponse;
import com.example.coordinator.service.CoordinatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class CoordinatorController {

    private final CoordinatorService coordinatorService;

    @PostMapping
    public ResponseEntity<TransactionResponse> executeTransaction(@RequestBody TransactionRequest request) {
        TransactionResponse response = coordinatorService.executeThreePhaseCommit(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransactionStatus(@PathVariable String transactionId) {
        TransactionResponse response = coordinatorService.getTransactionStatus(transactionId);
        return ResponseEntity.ok(response);
    }
}