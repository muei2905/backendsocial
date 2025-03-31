package com.example.BackEndSocial.repository;

import com.example.BackEndSocial.model.Post;
import com.example.BackEndSocial.model.PostLike;
import com.example.BackEndSocial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);

}
