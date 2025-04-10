package com.example.BackEndSocial.request;

import lombok.Data;

@Data
public class UpdateUserProfileRequest {
    private String fullName;
    private String avatar;
}
