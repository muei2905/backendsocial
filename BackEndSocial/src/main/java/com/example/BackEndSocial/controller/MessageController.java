package com.example.BackEndSocial.controller;

import com.example.BackEndSocial.DTO.ChatMessageDTO;
import com.example.BackEndSocial.model.Message;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.service.MessageService;
import com.example.BackEndSocial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody ChatMessageDTO message, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Message savedMessage = messageService.sendMessage(user, message);
        return ResponseEntity.ok(savedMessage);
    }

    @GetMapping("/between")
    public ResponseEntity<List<Message>> getMessagesBetween(@RequestHeader("Authorization") String jwt, @RequestParam Long receiverId) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        return ResponseEntity.ok(messageService.getMessagesBetween(user.getId(), receiverId));
    }

    @PatchMapping("/read/{id}")
    public ResponseEntity<Void> markAsRead(@PathVariable UUID id) {
        messageService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID id, @RequestHeader("Authorization") String jwt) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok().build();
    }
}
