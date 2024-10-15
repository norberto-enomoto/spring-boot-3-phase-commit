package com.example.notification.controller;

import com.example.notification.dto.TransactionInfo;
import com.example.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/canCommit")
    public ResponseEntity<Boolean> canCommit(@RequestBody TransactionInfo transactionInfo) {
        boolean canCommit = notificationService.canCommit(transactionInfo);
        return ResponseEntity.ok(canCommit);
    }

    @PostMapping("/preCommit")
    public ResponseEntity<Void> preCommit(@RequestBody TransactionInfo transactionInfo) {
        notificationService.preCommit(transactionInfo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/doCommit")
    public ResponseEntity<Void> doCommit(@RequestBody TransactionInfo transactionInfo) {
        notificationService.doCommit(transactionInfo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/abort")
    public ResponseEntity<Void> abort(@RequestBody TransactionInfo transactionInfo) {
        notificationService.abort(transactionInfo);
        return ResponseEntity.ok().build();
    }
}