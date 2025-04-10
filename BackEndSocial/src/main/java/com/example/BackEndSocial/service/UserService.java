package com.example.BackEndSocial.service;

import com.example.BackEndSocial.model.User;

public interface UserService {
    public User findUserByJwtToken(String jwt) throws Exception;
    public User findUserByEmail(String email) throws Exception;
    User updateUserProfile(Long userId, String fullName, String avatar);

}
