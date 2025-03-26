package com.example.BackEndSocial.service;

import com.example.BackEndSocial.DTO.ChatMessageDTO;
import com.example.BackEndSocial.model.Message;
import com.example.BackEndSocial.model.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message sendMessage(User user, ChatMessageDTO message);
    List<Message> getMessagesBetween(Long senderId, Long receiverId);
    void markAsRead(UUID messageId);
    void deleteMessage(UUID messageId);

}
