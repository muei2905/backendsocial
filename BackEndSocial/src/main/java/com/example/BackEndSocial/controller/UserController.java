package com.example.BackEndSocial.controller;

import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.request.UpdateUserProfileRequest;
import com.example.BackEndSocial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> findUserByJwtToken(@RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @PutMapping("/{id}/profile")
    public ResponseEntity<User> updateProfile(
            @PathVariable Long id,
            @RequestBody UpdateUserProfileRequest request) {
        User updatedUser = userService.updateUserProfile(id, request.getFullName(), request.getAvatar());
        return ResponseEntity.ok(updatedUser);
    }
}