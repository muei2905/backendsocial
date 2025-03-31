package com.example.BackEndSocial.service;

import com.example.BackEndSocial.model.Comment;
import com.example.BackEndSocial.model.Post;
import com.example.BackEndSocial.model.User;

public interface CommentService {
    public Comment commentPost(Post post, User user, String content);
    boolean deleteComment(Long commentId, User user);

}
