package com.example.BackEndSocial.repository;

import com.example.BackEndSocial.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findPostByUserId(Long userId);
}
