package com.example.BackEndSocial.service;

import com.example.BackEndSocial.DTO.ChatMessageDTO;
import com.example.BackEndSocial.model.Message;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.repository.MessageRepository;
import com.example.BackEndSocial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    @Override
    public Message sendMessage(User user, ChatMessageDTO message) {
        Message savedMessage = new Message();
        savedMessage.setContent(message.getContent());
        Optional<User> user1=  userRepository.findById(message.getReceiverID());
        savedMessage.setReceiverId(user1.get());
        savedMessage.setSenderId(user);
        savedMessage.setContent(message.getContent());
        savedMessage.setPicture(message.getPicture());
        savedMessage.setTimestamp(String.valueOf(LocalDateTime.now()));
        messagingTemplate.convertAndSendToUser(
                String.valueOf(message.getReceiverID()), "/queue/messages", savedMessage
        );
        return messageRepository.save(savedMessage);
    }


    @Override
    public void deleteMessage(UUID messageId) {
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
    public void markAsRead(UUID messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow();
        message.setRead(true);
        messageRepository.save(message);
    }
}
