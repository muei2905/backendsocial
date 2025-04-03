package com.example.BackEndSocial.repository;

import com.example.BackEndSocial.model.Message;
import com.example.BackEndSocial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findBySenderIdAndReceiverId(User senderId, User receiverId);
    @Query("SELECT DISTINCT u FROM User u " +
            "WHERE u.id IN (SELECT m.sender.id FROM Message m WHERE m.receiver.id = :userId) " +
            "OR u.id IN (SELECT m.receiver.id FROM Message m WHERE m.sender.id = :userId)")
    List<User> findContactsByUserId(@Param("userId") Long userId);
}
