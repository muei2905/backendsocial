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
        System.out.println("Received payload: " + payload);
        try {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setSender(Long.parseLong(payload.get("senderId").toString()));
            messageDTO.setReceiver(Long.parseLong(payload.get("receiverId").toString()));
            messageDTO.setContent(payload.get("content").toString());
            messageDTO.setPicture(payload.get("picture") != null ? payload.get("picture").toString() : null);
            messageDTO.setTimeStamp(String.valueOf(System.currentTimeMillis()));

            // Lưu vào DB trước khi gửi
            System.out.println("Saving message to DB: " + messageDTO);
            Message message = messageService.saveMessage(messageDTO);
            System.out.println("Save successful: " + message);

            // Gửi tin nhắn sau khi lưu thành công
            System.out.println("Sending to sender: " + messageDTO.getSender());
            messagingTemplate.convertAndSendToUser(messageDTO.getSender().toString(), "/queue/messages", messageDTO);
            System.out.println("Sending to receiver: " + messageDTO.getReceiver());
            messagingTemplate.convertAndSendToUser(messageDTO.getReceiver().toString(), "/queue/messages", messageDTO);
            System.out.println("Send successful to users");
        } catch (Exception e) {
            System.err.println("Error in handlePrivateMessage: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
