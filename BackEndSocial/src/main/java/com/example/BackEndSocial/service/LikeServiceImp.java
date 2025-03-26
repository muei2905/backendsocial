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
    @Autowired
    private UserRepository userRepository;
    @Override
    public PostLike likePost(Post post, User user) {
        PostLike postLike = new PostLike();
        postLike.setPost(post);
        postLike.setUser(user);
        return likeRepository.save(postLike);
    }
}
