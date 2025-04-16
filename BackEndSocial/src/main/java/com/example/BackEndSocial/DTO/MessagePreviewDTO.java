package com.example.BackEndSocial.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MessagePreviewDTO {
    private String content;
    private boolean sentByCurrentUser;
    private String timestamp;
    private boolean isDeleted;
}
