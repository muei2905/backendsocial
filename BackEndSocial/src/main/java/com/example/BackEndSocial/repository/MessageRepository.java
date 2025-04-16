package com.example.BackEndSocial.repository;

import com.example.BackEndSocial.model.Message;
import com.example.BackEndSocial.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndReceiver(User sender, User receiver);

    @Query("SELECT m FROM Message m WHERE " +
            "((m.sender.id = :userId1 AND m.receiver.id = :userId2) OR " +
            " (m.sender.id = :userId2 AND m.receiver.id = :userId1)) AND " +
            "ORDER BY m.timestamp DESC")
    Page<Message> findMessagesBetweenPaged(Long userId1, Long userId2, Pageable pageable);


    // Truy vấn danh sách user đã từng nhắn tin với userId
    @Query("SELECT DISTINCT m.sender FROM Message m WHERE m.receiver.id = :userId AND m.sender.id <> :userId " +
            "UNION " +
            "SELECT DISTINCT m.receiver FROM Message m WHERE m.sender.id = :userId AND m.receiver.id <> :userId")
    List<User> findContactsByUserId(@Param("userId") Long userId);

    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender.id = :userId AND m.receiver.id = :contactId) OR " +
            "(m.sender.id = :contactId AND m.receiver.id = :userId) " +
            "ORDER BY m.timestamp DESC")
    List<Message> findLastMessageBetween(@Param("userId") Long userId, @Param("contactId") Long contactId);


    @Modifying
    @Query("DELETE FROM Message m WHERE m.isDeleted = true AND m.deletedAt <= :threshold")
    void deleteMessagesDeletedBefore(LocalDateTime threshold);

}
