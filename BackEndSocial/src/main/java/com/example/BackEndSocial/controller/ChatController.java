package com.example.BackEndSocial.controller;

import com.example.BackEndSocial.DTO.MessageDTO;
import com.example.BackEndSocial.model.Message;
import com.example.BackEndSocial.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
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
        MessageDTO messageDTO= new MessageDTO();
        messageDTO.setSender(Long.parseLong(payload.get("senderId").toString()));
        messageDTO.setReceiver(Long.parseLong(payload.get("receiverId").toString()));
        messageDTO.setContent(payload.get("content").toString());
        messageDTO.setPicture(payload.get("picture") != null ? payload.get("picture").toString() : null);
        messageDTO.setTimeStamp(String.valueOf(System.currentTimeMillis()));
        messagingTemplate.convertAndSendToUser(messageDTO.getSender().toString(), "/queue/messages", messageDTO);
        messagingTemplate.convertAndSendToUser(messageDTO.getReceiver().toString(), "/queue/messages", messageDTO);
        Message message = messageService.saveMessage(messageDTO);
    }
}
