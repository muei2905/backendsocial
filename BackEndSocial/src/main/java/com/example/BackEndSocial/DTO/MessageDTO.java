package com.example.BackEndSocial.DTO;

import com.example.BackEndSocial.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class MessageDTO {
    private Long senderId;
    private Long receiverId;
    private String content;
    private String picture;
    private String timeStamp;
}
