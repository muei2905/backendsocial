package com.example.BackEndSocial.controller;

import com.example.BackEndSocial.DTO.MessageDTO;
import com.example.BackEndSocial.model.Message;
import com.example.BackEndSocial.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;
@Slf4j
@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @MessageMapping("/chat")
    public Message recMessage(@Payload MessageDTO messageDTO, Principal principal, @Header("simpSessionId") String sessionId) {
        messagingTemplate.convertAndSendToUser(String.valueOf(messageDTO.getReceiverId()), "/queue/messages", messageDTO);
        messagingTemplate.convertAndSendToUser(String.valueOf(messageDTO.getSenderId()), "/queue/messages", messageDTO);
        Message message = messageService.saveMessage(messageDTO);
        return message;
    }

}
