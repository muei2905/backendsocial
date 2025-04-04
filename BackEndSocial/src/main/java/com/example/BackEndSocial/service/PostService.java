package com.example.BackEndSocial.service;

import com.example.BackEndSocial.DTO.PostDTO;
import com.example.BackEndSocial.model.Post;
import com.example.BackEndSocial.model.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    public Post createPost(PostDTO req, User user);
    public Post updatePost(Long postId, PostDTO req);
    public void deletePost(Long postId);
    public List<Post> getPostsByUser(Long userId);
    public List<Post> getPostsForUser(Long userId);
}
