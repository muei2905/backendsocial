package com.example.BackEndSocial.repository;

import com.example.BackEndSocial.model.Message;
import com.example.BackEndSocial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findBySenderIdAndReceiverId(User senderId, User receiverId);

    // Truy vấn danh sách user đã từng nhắn tin với userId
    @Query("SELECT DISTINCT m.sender FROM Message m WHERE m.receiver.id = :userId " +
            "UNION " +
            "SELECT DISTINCT m.receiver FROM Message m WHERE m.sender.id = :userId")
    List<User> findContactsByUserId(@Param("userId") Long userId);
}
