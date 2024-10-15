package com.example.payment.service;

import com.example.payment.dto.TransactionInfo;
import com.example.payment.model.Payment;
import com.example.payment.repository.PaymentRepository;

import jakarta.transaction.InvalidTransactionException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Value("${payment.minimum.amount:0.01}")
    private double minimumTransactionAmount;

    @Value("${payment.maximum.amount:10000.00}")
    private double maximumTransactionAmount;

    @Transactional(readOnly = true)
    public boolean canCommit(TransactionInfo transactionInfo) throws InvalidTransactionException {
        validateTransaction(transactionInfo);

        // Aqui você pode adicionar lógicas adicionais de verificação
        // Por exemplo, verificar se o método de pagamento é válido
        boolean isPaymentMethodValid = checkPaymentMethod(transactionInfo);

        // Simular uma verificação de crédito ou saldo
        boolean hasSufficientFunds = simulateCreditCheck(transactionInfo);

        return isPaymentMethodValid && hasSufficientFunds;
    }

    private void validateTransaction(TransactionInfo transactionInfo) throws InvalidTransactionException {
        if (transactionInfo.getAmount() < minimumTransactionAmount) {
            throw new InvalidTransactionException("Transaction amount is below the minimum allowed: " + minimumTransactionAmount);
        }
        if (transactionInfo.getAmount() > maximumTransactionAmount) {
            throw new InvalidTransactionException("Transaction amount exceeds the maximum allowed: " + maximumTransactionAmount);
        }
    }

    private boolean checkPaymentMethod(TransactionInfo transactionInfo) {
        // Simular uma verificação do método de pagamento
        // Na prática, isso poderia envolver a verificação de um token de pagamento válido
        return true; // Simplificado para este exemplo
    }

    private boolean simulateCreditCheck(TransactionInfo transactionInfo) {
        // Simular uma verificação de crédito ou saldo
        // Na prática, isso poderia envolver uma chamada a um serviço externo de pagamento
        return transactionInfo.getAmount() >= 100.00; // Exemplo: limite de crédito de 5000
    }
    @Transactional
    public void preCommit(TransactionInfo transactionInfo) {
        Payment payment = new Payment();
        payment.setTransactionId(transactionInfo.getTransactionId());
        payment.setAmount(transactionInfo.getAmount());
        payment.setStatus("RESERVED");
        paymentRepository.save(payment);
    }

    @Transactional
    public void doCommit(TransactionInfo transactionInfo) {
        Payment payment = paymentRepository.findByTransactionId(transactionInfo.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setStatus("COMPLETED");
        paymentRepository.save(payment);
    }

    @Transactional
    public void abort(TransactionInfo transactionInfo) {
        paymentRepository.findByTransactionId(transactionInfo.getTransactionId())
                .ifPresent(payment -> {
                    if ("RESERVED".equals(payment.getStatus())) {
                        payment.setStatus("CANCELLED");
                        paymentRepository.save(payment);
                    }
                });
    }
}