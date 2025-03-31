package com.example.BackEndSocial.controller;

import com.example.BackEndSocial.DTO.PostDTO;
import com.example.BackEndSocial.model.Post;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.service.PostService;
import com.example.BackEndSocial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostDTO postDto, @RequestHeader("Authorization") String jwt) throws Exception {
        User user= userService.findUserByJwtToken(jwt);
        Post post = postService.createPost(postDto, user);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody PostDTO updatedPostDto, @RequestHeader("Authorization") String token) {
        Post updatedPost = postService.updatePost(postId, updatedPostDto);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, @RequestHeader("Authorization") String token) {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/feed")
    public ResponseEntity<List<Post>> getUserFeed(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Post> posts = postService.getPostsForUser(user.getId());
        return ResponseEntity.ok(posts);
    }
}
