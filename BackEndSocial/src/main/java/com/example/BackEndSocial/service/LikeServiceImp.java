package com.example.BackEndSocial.service;

import com.example.BackEndSocial.model.Post;
import com.example.BackEndSocial.model.PostLike;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.repository.LikeRepository;
import com.example.BackEndSocial.repository.PostRepository;
import com.example.BackEndSocial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeServiceImp implements LikeService{
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private PostRepository postRepository;

    public PostLike toggleLike(User user, Post post) {
        Optional<PostLike> existingLike = likeRepository.findByUserAndPost(user, post);

        if (existingLike.isPresent()) {
            PostLike like = existingLike.get();
            like.setLike(!like.isLike()); // Đảo trạng thái
            likeRepository.save(like);

            return like.isLike() ? like : null;
        } else {
            PostLike newLike = new PostLike();
            newLike.setLike(true);
            newLike.setUser(user);
            newLike.setPost(post);
            likeRepository.save(newLike);
            return newLike;
        }
    }
}
