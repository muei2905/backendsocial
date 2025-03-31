package com.example.BackEndSocial.controller;

import com.example.BackEndSocial.model.Message;
import com.example.BackEndSocial.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;
@Slf4j
@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat")
    public void handlePrivateMessage(Map<String, Object> payload) {
        Long senderId = Long.parseLong(payload.get("senderId").toString());
        Long receiverId = Long.parseLong(payload.get("receiverId").toString());
        String content = payload.get("content").toString();
        String picture = payload.get("picture") != null ? payload.get("picture").toString() : null;

        Message message = messageService.saveMessage(senderId, receiverId, content, picture);

        messagingTemplate.convertAndSendToUser(senderId.toString(), "/queue/messages", message);

        messagingTemplate.convertAndSendToUser(receiverId.toString(), "/queue/messages", message);
    }
}
