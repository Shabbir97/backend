package com.propertyfinder.repository;

import com.propertyfinder.entity.Notification;
import com.propertyfinder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipientOrderByCreatedAtDesc(User recipient);

    List<Notification> findByRecipientAndReadFalse(User recipient);

    long countByRecipientAndReadFalse(User recipient);
}