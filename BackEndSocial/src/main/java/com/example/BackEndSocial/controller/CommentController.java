package com.example.BackEndSocial.controller;

import com.example.BackEndSocial.DTO.CommentDTO;
import com.example.BackEndSocial.model.Comment;
import com.example.BackEndSocial.model.Post;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.rabbit.CommentNotificationProducer;

import com.example.BackEndSocial.repository.PostRepository;
import com.example.BackEndSocial.service.CommentService;
import com.example.BackEndSocial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentNotificationProducer commentNotificationProducer;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @PostMapping()
    public ResponseEntity<String> commentPost(@RequestBody CommentDTO req, @RequestHeader("Authorization") String jwt) throws Exception {
        Optional<Post> post = postRepository.findById(req.getPostId());
        User user = userService.findUserByJwtToken(jwt);

        Comment comment = commentService.commentPost(post.get(), user, req.getContent());
        if (comment != null) {
            String messageWithEmail = user.getFullName() + "|" + req.getContent() + "|" + post.get().getUser().getEmail();
            commentNotificationProducer.sendCommentNotification(messageWithEmail);
            return ResponseEntity.ok("Bình luận thành công!");
        }
        return ResponseEntity.badRequest().body("Bình luận thất bại!");
    }
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        boolean isDeleted = commentService.deleteComment(commentId, user);

        if (isDeleted) {
            return ResponseEntity.ok("Bình luận đã được xóa!");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không thể xóa bình luận này!");
    }
}
