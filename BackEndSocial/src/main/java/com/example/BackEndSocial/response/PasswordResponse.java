package com.example.BackEndSocial.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordResponse {
    private String token;
    private String message;
}
