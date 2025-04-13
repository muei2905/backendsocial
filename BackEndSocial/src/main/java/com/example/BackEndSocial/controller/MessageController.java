package com.example.BackEndSocial.controller;

import com.example.BackEndSocial.DTO.ChatMessageDTO;
import com.example.BackEndSocial.DTO.ContactPreviewDTO;
import com.example.BackEndSocial.model.Message;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.service.MessageService;
import com.example.BackEndSocial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    @GetMapping("/between")
    public ResponseEntity<Map<String, Object>> getMessagesBetween(
            @RequestHeader("Authorization") String jwt,
            @RequestParam Long receiverId,
            @RequestParam int page,
            @RequestParam(defaultValue = "20") int size) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Page<Message> pageMessages = messageService.getMessagesBetween(user.getId(), receiverId, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("messages", pageMessages.getContent());
        response.put("currentPage", pageMessages.getNumber());
        response.put("totalItems", pageMessages.getTotalElements());
        response.put("totalPages", pageMessages.getTotalPages());

        return ResponseEntity.ok(response);
    }



    @PatchMapping("/read/{id}")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id, @RequestHeader("Authorization") String jwt) {
        messageService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id, @RequestHeader("Authorization") String jwt) throws Exception {
        messageService.deleteMessage(id, jwt);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/contacts")
    public ResponseEntity<List<ContactPreviewDTO>> getContactsWithLastMessage(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<ContactPreviewDTO> contacts = messageService.getContactsWithLastMessage(user.getId());
        return ResponseEntity.ok(contacts);
    }


}
