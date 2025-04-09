package com.example.BackEndSocial.service;

import com.example.BackEndSocial.model.Comment;
import com.example.BackEndSocial.model.Post;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.repository.CommentRepository;
import com.example.BackEndSocial.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImp implements CommentService{
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public Comment commentPost(Post post, User user, String content) {
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);
        // +1 vào totalCmt
        post.setTotalCmt(post.getTotalCmt() + 1);

        postRepository.save(post); // Cập nhật lại post
        return commentRepository.save(comment);
    }

    @Override
    public boolean deleteComment(Long commentId, User user) {
        Optional<Comment> commentOpt = commentRepository.findByIdAndUser(commentId, user);
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();
            Post post = comment.getPost();
            post.setTotalCmt(Math.max(0, post.getTotalCmt() - 1));
            postRepository.save(post);
            commentRepository.delete(comment);
            return true;
        }
        return false;
    }

}
