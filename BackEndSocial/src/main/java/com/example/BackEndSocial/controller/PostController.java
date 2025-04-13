package com.example.BackEndSocial.controller;

import com.example.BackEndSocial.DTO.PostDTO;
import com.example.BackEndSocial.model.Post;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.response.PostResponse;
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
    public ResponseEntity<?> deletePost(@PathVariable Long postId, @RequestHeader("Authorization") String jwt) {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/me")
    public ResponseEntity<List<PostResponse>> getMyPosts(@RequestHeader("Authorization") String jwt) throws Exception {

        User user= userService.findUserByJwtToken(jwt);
        List<PostResponse> posts = postService.getPostsByUser(user.getId(), user);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getPostsByUserId(@PathVariable Long userId, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<PostResponse> posts = postService.getPostsByUser(userId, user);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostResponse>> getUserFeed(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<PostResponse> posts = postService.getPostsForUser(user.getId(), page, size, user);
        return ResponseEntity.ok(posts);
    }
}
