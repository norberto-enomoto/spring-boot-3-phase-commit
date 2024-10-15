package com.example.coordinator.service;

import com.example.coordinator.client.TicketServiceClient;
import com.example.coordinator.client.PaymentServiceClient;
import com.example.coordinator.client.NotificationServiceClient;
import com.example.coordinator.dto.TransactionInfo;
import com.example.coordinator.dto.TransactionRequest;
import com.example.coordinator.dto.TransactionResponse;
import com.example.coordinator.model.Transaction;
import com.example.coordinator.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CoordinatorService {

    private final TransactionRepository transactionRepository;
    private final TicketServiceClient ticketServiceClient;
    private final PaymentServiceClient paymentServiceClient;
    private final NotificationServiceClient notificationServiceClient;

    @Transactional
    public TransactionResponse executeThreePhaseCommit(TransactionRequest request) {
        String transactionId = UUID.randomUUID().toString();
        
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setStatus("INITIATED");
        transaction.setTicketId(request.getTicketId());
        transaction.setAmount(request.getAmount());
        transaction = transactionRepository.save(transaction);

        TransactionInfo transactionInfo = new TransactionInfo();
        transactionInfo.setTransactionId(transactionId);
        transactionInfo.setTicketId(request.getTicketId());
        transactionInfo.setAmount(request.getAmount());

        try {
            // Phase 1: Can-commit
            boolean canCommitTicket = ticketServiceClient.canCommit(transactionInfo);
            boolean canCommitPayment = paymentServiceClient.canCommit(transactionInfo);
            boolean canCommitNotification = notificationServiceClient.canCommit(transactionInfo);

            if (canCommitTicket && canCommitPayment && canCommitNotification) {
                // Phase 2: Pre-commit
                transaction.setStatus("PRE_COMMIT");
                transactionRepository.save(transaction);

                ticketServiceClient.preCommit(transactionInfo);
                paymentServiceClient.preCommit(transactionInfo);
                notificationServiceClient.preCommit(transactionInfo);

                // Phase 3: Do-commit
                transaction.setStatus("COMMITTING");
                transactionRepository.save(transaction);

                ticketServiceClient.doCommit(transactionInfo);
                paymentServiceClient.doCommit(transactionInfo);
                notificationServiceClient.doCommit(transactionInfo);

                transaction.setStatus("COMMITTED");
                transactionRepository.save(transaction);

                return createResponse(transaction, "Transaction completed successfully");
            } else {
                abortTransaction(transaction, transactionInfo);
                return createResponse(transaction, "Transaction aborted: Cannot commit");
            }
        } catch (Exception e) {
            abortTransaction(transaction, transactionInfo);
            return createResponse(transaction, "Transaction failed: " + e.getMessage());
        }
    }

    private void abortTransaction(Transaction transaction, TransactionInfo transactionInfo) {
        transaction.setStatus("ABORTING");
        transactionRepository.save(transaction);

        try {
            ticketServiceClient.abort(transactionInfo);
        } catch (Exception e) {
            // Log error, but continue aborting
        }
        try {
            paymentServiceClient.abort(transactionInfo);
        } catch (Exception e) {
            // Log error, but continue aborting
        }
        try {
            notificationServiceClient.abort(transactionInfo);
        } catch (Exception e) {
            // Log error, but continue aborting
        }

        transaction.setStatus("ABORTED");
        transactionRepository.save(transaction);
    }

    private TransactionResponse createResponse(Transaction transaction, String message) {
        TransactionResponse response = new TransactionResponse();
        response.setTransactionId(transaction.getTransactionId());
        response.setStatus(transaction.getStatus());
        response.setMessage(message);
        return response;
    }

    public TransactionResponse getTransactionStatus(String transactionId) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return createResponse(transaction, "Transaction status retrieved");
    }
}