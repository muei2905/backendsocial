package com.example.BackEndSocial.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String imageUrl;
    private LocalDateTime createAt;
    private int totalLike;
    private int totalCmt;
    private String viewMode;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
