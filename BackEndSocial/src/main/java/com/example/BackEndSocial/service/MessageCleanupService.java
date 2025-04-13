package com.example.BackEndSocial.service;

import com.example.BackEndSocial.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageCleanupService {
    @Autowired
    private  MessageRepository messageRepository;

    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void deleteOldSoftDeletedMessages() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        messageRepository.deleteMessagesDeletedBefore(thirtyDaysAgo);
    }
}
