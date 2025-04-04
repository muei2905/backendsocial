package com.example.BackEndSocial.service;

import com.example.BackEndSocial.model.Comment;
import com.example.BackEndSocial.model.Post;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImp implements CommentService{
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment commentPost(Post post, User user, String content) {
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    @Override
    public boolean deleteComment(Long commentId, User user) {
        Optional<Comment> commentOpt = commentRepository.findByIdAndUser(commentId, user);
        if (commentOpt.isPresent()) {
            commentRepository.delete(commentOpt.get());
            return true;
        }
        return false;
    }
}
