package com.example.BackEndSocial.service;

import com.example.BackEndSocial.config.JwtProvider;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        User user = findUserByEmail(email);
        return user;
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user==null){
            throw new Exception("user not found");
        }
        return user;
    }

    @Override
    public User updateUserProfile(Long userId, String fullName, String avatar) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(fullName);
        user.setAvatar(avatar);

        return userRepository.save(user);
    }
}
