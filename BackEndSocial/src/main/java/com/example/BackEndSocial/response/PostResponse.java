package com.example.BackEndSocial.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class PostResponse {
    private Long id;
    private String content;
    private String imageUrl;
    private LocalDateTime createAt;
    private int totalLike;
    private int totalCmt;
    private String viewMode;
    private String userName;
    private String avatar;

    private List<CommentResponse> comments;
    private List<PostLikeUserResponse> likedUsers;
}
