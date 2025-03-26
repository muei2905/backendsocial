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
public class FriendNotificationConsumer {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationRepository notificationRepository;

    @RabbitListener(queues = "notification.friendship.queue")
    public void receiveNotification(String message) throws Exception {
        String[] parts = message.split("\\|");
        if (parts.length < 3) throw new IllegalArgumentException("Sai định dạng: " + message);
        String sender = parts[0];
        String action = parts[1];
        String receiverEmail = parts[parts.length - 1];
        String notificationMessage = sender + " " + action;
            User receiver = userService.findUserByEmail(receiverEmail);
            messagingTemplate.convertAndSendToUser(
                    String.valueOf(receiver.getId()), "/queue/notifications", notificationMessage);
        Notification notification = new Notification();
            notification.setUser(receiver);
            notification.setType("FRIENDSHIP");
            notification.setMessage(notificationMessage);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setRead(false);
            notificationRepository.save(notification);
    }
}
