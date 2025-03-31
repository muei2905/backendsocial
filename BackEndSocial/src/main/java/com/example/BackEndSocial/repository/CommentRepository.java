package com.example.BackEndSocial.repository;

import com.example.BackEndSocial.model.Comment;
import com.example.BackEndSocial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndUser(Long id, User user);

}
