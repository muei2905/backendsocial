package com.example.BackEndSocial.controller;

import com.example.BackEndSocial.model.Post;
import com.example.BackEndSocial.model.PostLike;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.rabbit.LikeNotificationProducer;
import com.example.BackEndSocial.repository.PostRepository;
import com.example.BackEndSocial.service.LikeService;
import com.example.BackEndSocial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@RestController
@RequestMapping("/likes")
public class LikeController {
    @Autowired
    private LikeService likeService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private LikeNotificationProducer notificationProducer;

    @PostMapping("/{postId}")
    public ResponseEntity<String> likePost(@PathVariable Long postId, @RequestHeader("Authorization") String jwt) throws Exception {
        Optional<Post> post = postRepository.findById(postId);

        User user = userService.findUserByJwtToken(jwt);

        PostLike like = likeService.likePost(post.get(), user);
        if (like != null) {
            String notificationMessage = user.getFullName() + " đã thích bài viết của bạn!";
            String messageWithEmail = notificationMessage + "|" + post.get().getUser().getEmail();
            notificationProducer.sendLikeNotification(messageWithEmail);
            return ResponseEntity.ok("Like thành công!");
        }
        return ResponseEntity.badRequest().body("Like thất bại!");
    }
}
