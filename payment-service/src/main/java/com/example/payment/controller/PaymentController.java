package com.example.payment.controller;

import com.example.payment.dto.TransactionInfo;
import com.example.payment.service.PaymentService;

import jakarta.transaction.InvalidTransactionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/canCommit")
    public ResponseEntity<Boolean> canCommit(@RequestBody TransactionInfo transactionInfo) throws InvalidTransactionException {
        boolean canCommit = paymentService.canCommit(transactionInfo);
        return ResponseEntity.ok(canCommit);
    }

    @PostMapping("/preCommit")
    public ResponseEntity<Void> preCommit(@RequestBody TransactionInfo transactionInfo) {
        paymentService.preCommit(transactionInfo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/doCommit")
    public ResponseEntity<Void> doCommit(@RequestBody TransactionInfo transactionInfo) {
        paymentService.doCommit(transactionInfo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/abort")
    public ResponseEntity<Void> abort(@RequestBody TransactionInfo transactionInfo) {
        paymentService.abort(transactionInfo);
        return ResponseEntity.ok().build();
    }
}