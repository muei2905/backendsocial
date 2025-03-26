package com.example.BackEndSocial.repository;

import com.example.BackEndSocial.model.Message;
import com.example.BackEndSocial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findBySenderIdAndReceiverId(User senderId, User receiverId);

}
