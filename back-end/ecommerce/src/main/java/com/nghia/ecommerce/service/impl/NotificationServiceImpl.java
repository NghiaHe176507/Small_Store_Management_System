package com.nghia.ecommerce.service.impl;

import com.nghia.ecommerce.dto.response.NotificationResponse;
import com.nghia.ecommerce.model.Notification;
import com.nghia.ecommerce.model.User;
import com.nghia.ecommerce.repository.NotificationRepository;
import com.nghia.ecommerce.repository.UserRepository;
import com.nghia.ecommerce.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepo;
    private final UserRepository userRepo;

    @Override
    public void sendNotification(Long userId, String message) {
        User recipient = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Notification notification = Notification.builder()
                .recipient(recipient)
                .message(message)
                .build();

        notificationRepo.save(notification);
    }

    @Override
    public List<NotificationResponse> getMyNotifications(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return notificationRepo.findByRecipientOrderByCreatedAtDesc(user)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

        if (!notification.getRecipient().getId().equals(userId)) {
            throw new AccessDeniedException("You cannot mark this notification");
        }

        notification.setIsRead(true);
        notificationRepo.save(notification);
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .read(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
