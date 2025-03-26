package com.example.BackEndSocial.DTO;

import lombok.Data;

@Data
public class NotificationDTO {
    private String type;
    private String message;
    private boolean isRead;
}
