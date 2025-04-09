package com.example.BackEndSocial.response;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class CommentResponse {
    private Long id;
    private String content;
    private String userName;
    private LocalDateTime time;
}
