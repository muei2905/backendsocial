package com.example.BackEndSocial.service;

import com.example.BackEndSocial.DTO.PostDTO;
import com.example.BackEndSocial.model.Post;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PostServiceImp implements PostService{
    @Autowired
    private PostRepository postRepository;


    @Override
    public Post createPost(PostDTO req, User user) {
        Post post = new Post();
        post.setUser(user);
        post.setContent(req.getContent());
        post.setImageUrl(req.getImageUrl());
        post.setCreateAt(LocalDateTime.now());
        post.setTotalLike(0);
        post.setTotalCmt(0);
        post.setViewMode(req.getViewMode() != null ? req.getViewMode() : "public");
        return postRepository.save(post);
    }

    @Override
    public Post updatePost(Long postId, PostDTO updatedPostDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setContent(updatedPostDto.getContent());
        post.setImageUrl(updatedPostDto.getImageUrl());
        post.setViewMode(updatedPostDto.getViewMode() != null ? updatedPostDto.getViewMode() : post.getViewMode());
        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        postRepository.deleteById(postId);
    }

    @Override
    public List<Post> getPostsByUser(Long userId) {
        return postRepository.findPostByUserId(userId);
    }
}
