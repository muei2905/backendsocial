package com.example.BackEndSocial.service;

import com.example.BackEndSocial.DTO.PostDTO;
import com.example.BackEndSocial.model.Post;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.response.PostResponse;

import java.util.List;
import java.util.UUID;

public interface PostService {
    public Post createPost(PostDTO req, User user);

    public Post updatePost(Long postId, PostDTO req);

    public void deletePost(Long postId);

    public List<PostResponse> getPostsByUser(Long userId);
    public List<PostResponse> getPostsForUser(Long userId, int page, int size);
}
