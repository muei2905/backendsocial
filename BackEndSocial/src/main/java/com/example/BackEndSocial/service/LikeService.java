package com.example.BackEndSocial.service;

import com.example.BackEndSocial.model.Post;
import com.example.BackEndSocial.model.PostLike;
import com.example.BackEndSocial.model.User;

public interface LikeService {
    public PostLike toggleLike(User user, Post post);

}
