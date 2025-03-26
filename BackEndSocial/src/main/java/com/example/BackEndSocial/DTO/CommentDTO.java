package com.example.BackEndSocial.DTO;

import lombok.Data;

@Data
public class CommentDTO {
    private Long postId;
    private String content;
}
