package com.example.BackEndSocial.DTO;

import lombok.Data;

@Data
public class ChatMessageDTO {
    private Long receiverID;
    private String content;
    private String timestamp;
    private String picture;
}
