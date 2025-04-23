package com.example.BackEndSocial.service;

import com.example.BackEndSocial.DTO.ContactPreviewDTO;
import com.example.BackEndSocial.DTO.MessageDTO;
import com.example.BackEndSocial.model.Message;
import com.example.BackEndSocial.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MessageService {
    public Page<Message> getMessagesBetween(Long userId, Long contactId, int page, int size);
    void markAsRead(Long messageId);
    public Message deleteMessage(Long messageId, String jwt) throws Exception;
    public Message saveMessage(MessageDTO messageDTO);
    public List<User> getUserContacts(Long userId);
    public List<ContactPreviewDTO> getContactsWithLastMessage(Long userId);
    public MessageDTO getMessageById(Long id);
}
