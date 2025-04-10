package com.example.BackEndSocial.service;

import com.example.BackEndSocial.DTO.ChatMessageDTO;
import com.example.BackEndSocial.DTO.ContactPreviewDTO;
import com.example.BackEndSocial.DTO.MessageDTO;
import com.example.BackEndSocial.model.Message;
import com.example.BackEndSocial.model.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    public Page<Message> getMessagesBetween(Long userId, Long contactId, int page, int size);
    void markAsRead(Long messageId);
    void deleteMessage(Long messageId);
    public Message saveMessage(MessageDTO messageDTO);
    public List<User> getUserContacts(Long userId);
    public List<ContactPreviewDTO> getContactsWithLastMessage(Long userId);
}
