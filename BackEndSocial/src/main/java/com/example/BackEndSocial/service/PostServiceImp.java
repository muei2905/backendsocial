package com.example.BackEndSocial.service;

import com.example.BackEndSocial.DTO.PostDTO;
import com.example.BackEndSocial.model.Post;
import com.example.BackEndSocial.model.PostLike;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.repository.PostRepository;
import com.example.BackEndSocial.response.CommentResponse;
import com.example.BackEndSocial.response.PostLikeUserResponse;
import com.example.BackEndSocial.response.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostServiceImp implements PostService{
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FriendService friendService;

    @Override
    public Post createPost(PostDTO req, User user) {
        Post post = new Post();
        post.setUser(user);
        post.setContent(req.getContent());
        post.setImageUrl(req.getImageUrl());
        post.setCreateAt(LocalDateTime.now());
        post.setTotalLike(0);
        post.setTotalCmt(0);
        post.setViewMode(req.getViewMode() != null ? req.getViewMode() : "public");
        return postRepository.save(post);
    }


    @Override
    public List<PostResponse> getPostsForUser(Long userId, int page, int size) {
        List<Long> friendIds = friendService.getFriendIds(userId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createAt").descending());
        Page<Post> postPage = postRepository.findPostsForUser(userId, friendIds, pageable);
        return postPage.stream().map(this::mapToPostResponse).collect(Collectors.toList());
    }

    @Override
    public Post updatePost(Long postId, PostDTO updatedPostDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setContent(updatedPostDto.getContent());
        post.setImageUrl(updatedPostDto.getImageUrl());
        post.setViewMode(updatedPostDto.getViewMode() != null ? updatedPostDto.getViewMode() : post.getViewMode());
        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        postRepository.deleteById(postId);
    }

    @Override
    public List<PostResponse> getPostsByUser(Long userId) {
        List<Post> posts = postRepository.findPostByUserId(userId);
        return posts.stream().map(this::mapToPostResponse).collect(Collectors.toList());
    }

    private PostResponse mapToPostResponse(Post post) {
        PostResponse dto = new PostResponse();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setImageUrl(post.getImageUrl());
        dto.setCreateAt(post.getCreateAt());
        dto.setTotalLike(post.getTotalLike());
        dto.setTotalCmt(post.getTotalCmt());
        dto.setViewMode(post.getViewMode());
        dto.setUserName(post.getUser().getFullName());
        dto.setAvatar(post.getUser().getAvatar());
        // Comments
        List<CommentResponse> commentDTOs = post.getComments().stream().map(cmt -> {
            CommentResponse cr = new CommentResponse();
            cr.setId(cmt.getId());
            cr.setContent(cmt.getContent());
            cr.setUserName(cmt.getUser().getFullName());
            cr.setTime(cmt.getTime());
            return cr;
        }).collect(Collectors.toList());
        dto.setComments(commentDTOs);

        // Likes
        List<PostLikeUserResponse> likedUsers = post.getLikes().stream()
                .filter(PostLike::isLike)
                .map(like -> {
                    PostLikeUserResponse lur = new PostLikeUserResponse();
                    lur.setId(like.getUser().getId());
                    lur.setUsername(like.getUser().getFullName());
                    return lur;
                }).collect(Collectors.toList());
        dto.setLikedUsers(likedUsers);

        return dto;
    }
}
