package com.example.BackEndSocial.response;

import com.example.BackEndSocial.model.USER_ROLE;
import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
    private String message;
    private USER_ROLE role;
}
