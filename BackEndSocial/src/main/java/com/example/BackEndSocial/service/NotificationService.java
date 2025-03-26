package com.example.BackEndSocial.service;

import com.example.BackEndSocial.DTO.NotificationDTO;
import com.example.BackEndSocial.model.Notification;
import com.example.BackEndSocial.model.User;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    Notification createNotification(NotificationDTO notificationDto, User user);
    List<Notification> getNotificationsByUser(Long token);
    void markAsRead(UUID id);
}
