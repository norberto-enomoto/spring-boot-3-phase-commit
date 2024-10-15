package com.example.notification.service;

import com.example.notification.dto.TransactionInfo;
import com.example.notification.model.Notification;
import com.example.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public boolean canCommit(TransactionInfo transactionInfo) {
        // Simular verificação se o serviço de notificação está disponível
        return true;
    }

    @Transactional
    public void preCommit(TransactionInfo transactionInfo) {
        Notification notification = new Notification();
        notification.setTransactionId(transactionInfo.getTransactionId());
        notification.setStatus("PENDING");
        notification.setMessage("Preparando notificação para a transação: " + transactionInfo.getTransactionId());
        notificationRepository.save(notification);
    }

    @Transactional
    public void doCommit(TransactionInfo transactionInfo) {
        Notification notification = notificationRepository.findByTransactionId(transactionInfo.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setStatus("SENT");
        notification.setMessage("Ingresso comprado com sucesso! Transação: " + transactionInfo.getTransactionId());
        notificationRepository.save(notification);
        
        // Simular o envio da notificação
        sendNotification(notification);
    }

    @Transactional
    public void abort(TransactionInfo transactionInfo) {
        notificationRepository.findByTransactionId(transactionInfo.getTransactionId())
                .ifPresent(notification -> {
                    notification.setStatus("CANCELLED");
                    notification.setMessage("Compra de ingresso cancelada. Transação: " + transactionInfo.getTransactionId());
                    notificationRepository.save(notification);
                    
                    // Simular o envio da notificação de cancelamento
                    sendNotification(notification);
                });
    }

    private void sendNotification(Notification notification) {
        // Simular o envio da notificação
        System.out.println("Enviando notificação: " + notification.getMessage());
    }
}