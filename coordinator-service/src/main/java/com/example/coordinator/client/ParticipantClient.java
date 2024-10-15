package com.example.coordinator.client;

import com.example.coordinator.dto.TransactionInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface ParticipantClient {
    @PostMapping("/canCommit")
    boolean canCommit(@RequestBody TransactionInfo transactionInfo);

    @PostMapping("/preCommit")
    void preCommit(@RequestBody TransactionInfo transactionInfo);

    @PostMapping("/doCommit")
    void doCommit(@RequestBody TransactionInfo transactionInfo);

    @PostMapping("/abort")
    void abort(@RequestBody TransactionInfo transactionInfo);
}
