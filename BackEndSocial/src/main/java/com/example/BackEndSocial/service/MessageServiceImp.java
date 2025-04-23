package com.example.BackEndSocial.service;

import com.example.BackEndSocial.DTO.ContactPreviewDTO;
import com.example.BackEndSocial.DTO.MessageDTO;
import com.example.BackEndSocial.DTO.MessagePreviewDTO;
import com.example.BackEndSocial.model.Message;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.repository.MessageRepository;
import com.example.BackEndSocial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImp implements MessageService{
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Async
    public void saveAsync(MessageDTO message) {
        saveMessage(message);
    }

    @Override
    public Message saveMessage(MessageDTO messageDTO) {

        User sender = userRepository.findById(messageDTO.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(messageDTO.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(messageDTO.getContent());
        message.setPicture(messageDTO.getPicture());
        message.setTimestamp(messageDTO.getTimeStamp());
        message.setRead(false);
        message.setDeleted(false);

        return messageRepository.save(message);
    }

    @Override
    public List<User> getUserContacts(Long userId) {
        return messageRepository.findContactsByUserId(userId);
    }
    @Override
    public List<ContactPreviewDTO> getContactsWithLastMessage(Long userId) {
        List<User> contacts = messageRepository.findContactsByUserId(userId);
        List<ContactPreviewDTO> result = new ArrayList<>();

        for (User contact : contacts) {
            List<Message> messages = messageRepository.findLastMessageBetween(userId, contact.getId());
            if (!messages.isEmpty()) {
                Message lastMessage = messages.get(0);
                boolean sentByCurrentUser = lastMessage.getSender().getId().equals(userId);
                MessagePreviewDTO preview = new MessagePreviewDTO(
                        lastMessage.getContent(),
                        sentByCurrentUser,
                        lastMessage.getTimestamp(),
                        lastMessage.isDeleted() // ➕ Truyền isDeleted về client
                );

                result.add(new ContactPreviewDTO(
                        contact.getId(),
                        contact.getFullName(),
                        contact.getAvatar(),
                        preview
                ));
            }
        }

        return result;
    }

    @Override
    public MessageDTO getMessageById(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(() -> new RuntimeException("Message not found"));
        return new MessageDTO(message.getId(),message.getSender().getId(),message.getReceiver().getId(), message.getContent(), message.getPicture(), message.getTimestamp());
    }


    @Override
    public Message deleteMessage(Long messageId, String jwt) throws Exception {
        User currentUser = userService.findUserByJwtToken(jwt);
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        // Kiểm tra người dùng hiện tại có phải là người gửi không
        if (!message.getSender().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to delete this message.");
        }

        message.setDeleted(true);
        message.setDeletedAt(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        messageRepository.save(message);
        return message;
    }

    @Override
    public Page<Message> getMessagesBetween(Long userId, Long contactId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return messageRepository.findMessagesBetweenPaged(userId, contactId, pageable);
    }


    @Override
    public void markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow();
        message.setRead(true);
        messageRepository.save(message);
    }
}
