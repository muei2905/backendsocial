package com.example.BackEndSocial.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContactPreviewDTO {
    private Long userId;
    private String fullName;
    private String avatarUrl;
    private MessagePreviewDTO lastMessage;
}
