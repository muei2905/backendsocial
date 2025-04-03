package com.example.BackEndSocial.service;

import com.example.BackEndSocial.DTO.ChatMessageDTO;
import com.example.BackEndSocial.model.Message;
import com.example.BackEndSocial.model.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    List<Message> getMessagesBetween(Long senderId, Long receiverId);
    void markAsRead(UUID messageId);
    void deleteMessage(UUID messageId);
    public Message saveMessage(Long senderId, Long receiverId, String content, String picture);
    public List<User> getUserContacts(Long userId);
}
