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
public class CommentNotificationConsumer {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationRepository notificationRepository;

    @RabbitListener(queues = "notification.comment.queue")
    public void handleCommentNotification(String message) throws Exception {
            String[] parts = message.split("\\|", 3);
            if (parts.length != 3) throw new IllegalArgumentException("Sai định dạng: " + message);
            String fullName = parts[0];
            String content = parts[1];
            String email = parts[2];
            String notificationMessage = fullName + " đã bình luận: \"" + content + "\"";


            User user = userService.findUserByEmail(email);
            messagingTemplate.convertAndSendToUser(String.valueOf(user.getId()), "/queue/notifications", notificationMessage);
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setType("COMMENT");
            notification.setMessage(content);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setRead(false);
            notificationRepository.save(notification);

            System.out.println("📥 Đã lưu thông báo bình luận vào DB: " + content);


    }

}
