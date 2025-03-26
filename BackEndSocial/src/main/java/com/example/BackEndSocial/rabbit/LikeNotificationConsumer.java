package com.example.BackEndSocial.rabbit;

import com.example.BackEndSocial.model.Notification;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.repository.NotificationRepository;
import com.example.BackEndSocial.service.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LikeNotificationConsumer {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserService userService;

    @RabbitListener(queues = "notification.queue")
    public void handleLikeNotification(String message) throws Exception {
        String[] parts = message.split("\\|");
        String notificationMessage = parts[0];
        String email = parts[1];
        User user =userService.findUserByEmail(email);
        messagingTemplate.convertAndSendToUser(String.valueOf(user.getId()), "/queue/notifications", notificationMessage);
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType("LIKE");
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        notificationRepository.save(notification);
    }

}

