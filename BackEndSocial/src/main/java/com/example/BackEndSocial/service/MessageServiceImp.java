package com.example.BackEndSocial.service;

import com.example.BackEndSocial.DTO.ChatMessageDTO;
import com.example.BackEndSocial.DTO.MessageDTO;
import com.example.BackEndSocial.model.Message;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.repository.MessageRepository;
import com.example.BackEndSocial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class MessageServiceImp implements MessageService{
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @Async
    public void saveMessageAsync(MessageDTO messageDTO) {
        saveMessage(messageDTO); // gọi method bình thường
    }

    @Override
    public Message saveMessage(MessageDTO messageDTO) {
        User sender = userRepository.findById(messageDTO.getSender())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(messageDTO.getReceiver())
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
    public void deleteMessage(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow();
        message.setDeleted(true);
        messageRepository.save(message);
    }
    @Override
    public List<Message> getMessagesBetween(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId).orElseThrow();
        User receiver = userRepository.findById(receiverId).orElseThrow();
        return messageRepository.findBySenderIdAndReceiverId(sender, receiver);
    }


    @Override
    public void markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow();
        message.setRead(true);
        messageRepository.save(message);
    }
}
