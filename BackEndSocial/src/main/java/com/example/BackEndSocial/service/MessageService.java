package com.example.BackEndSocial.service;

import com.example.BackEndSocial.DTO.ChatMessageDTO;
import com.example.BackEndSocial.DTO.MessageDTO;
import com.example.BackEndSocial.model.Message;
import com.example.BackEndSocial.model.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    List<Message> getMessagesBetween(Long senderId, Long receiverId);
    void markAsRead(Long messageId);
    void deleteMessage(Long messageId);
    public Message saveMessage(MessageDTO messageDTO);
    public List<User> getUserContacts(Long userId);
}
