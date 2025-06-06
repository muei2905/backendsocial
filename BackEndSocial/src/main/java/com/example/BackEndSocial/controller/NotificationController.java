package com.example.BackEndSocial.controller;

import com.example.BackEndSocial.DTO.NotificationDTO;
import com.example.BackEndSocial.model.Notification;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.service.NotificationService;
import com.example.BackEndSocial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<List<Notification>> getUserNotifications(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Notification> notifications = notificationService.getNotificationsByUser(user.getId());
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id, @RequestHeader("Authorization") String jwt) {
        notificationService.markAsRead(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
