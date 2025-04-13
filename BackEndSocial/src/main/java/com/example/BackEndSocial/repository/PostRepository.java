package com.example.BackEndSocial.repository;

import com.example.BackEndSocial.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findPostByUserId(Long userId);


    @Query("SELECT p FROM Post p WHERE p.user.id IN :friendIds OR p.user.id = :userId ORDER BY p.createAt DESC")
    List<Post> findPostsForUser(@Param("userId") Long userId, @Param("friendIds") List<Long> friendIds);

    @Query("SELECT p FROM Post p WHERE p.user.id IN :friendIds OR p.user.id = :userId ORDER BY p.createAt DESC")
    Page<Post> findPostsForUser(
            @Param("userId") Long userId,
            @Param("friendIds") List<Long> friendIds,
            Pageable pageable
    );
}
