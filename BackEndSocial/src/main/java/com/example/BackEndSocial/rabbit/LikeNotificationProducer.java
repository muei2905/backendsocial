package com.example.BackEndSocial.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeNotificationProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendLikeNotification(String message) {
        rabbitTemplate.convertAndSend("notification.exchange", "notification.like", message);
    }
}

