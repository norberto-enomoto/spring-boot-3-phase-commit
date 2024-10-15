package com.example.notification.repository;

import com.example.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByTransactionId(String transactionId);
}