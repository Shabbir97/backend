package com.propertyfinder.service;

import com.propertyfinder.dto.NotificationResponse;
import com.propertyfinder.entity.Notification;
import com.propertyfinder.entity.User;
import com.propertyfinder.repository.NotificationRepository;
import com.propertyfinder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(String email) {
        User user = getUser(email);
        return notificationRepository
                .findByRecipientOrderByCreatedAtDesc(user)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public NotificationResponse markRead(Long id, String email) {
        User user = getUser(email);
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Notification not found"));

        if (!notification.getRecipient().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Not your notification");
        }

        notification.setRead(true);
        return toDto(notificationRepository.save(notification));
    }

    public void markAllRead(String email) {
        User user = getUser(email);
        List<Notification> unread =
                notificationRepository.findByRecipientAndReadFalse(user);
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    }

    // Called internally by TenantRequestService when events happen
    public void create(User recipient, String message, String type,
                       Long relatedPropertyId, Long relatedRequestId) {
        Notification notification = Notification.builder()
                .recipient(recipient)
                .message(message)
                .type(type)
                .read(false)
                .relatedPropertyId(relatedPropertyId)
                .relatedRequestId(relatedRequestId)
                .build();
        notificationRepository.save(notification);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
    }

    private NotificationResponse toDto(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .message(n.getMessage())
                .type(n.getType())
                .read(n.isRead())
                .relatedPropertyId(n.getRelatedPropertyId())
                .relatedRequestId(n.getRelatedRequestId())
                .createdAt(n.getCreatedAt())
                .build();
    }
}