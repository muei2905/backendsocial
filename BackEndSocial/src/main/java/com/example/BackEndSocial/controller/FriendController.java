package com.example.BackEndSocial.controller;

import com.example.BackEndSocial.model.Friendship;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.rabbit.FriendNotificationProducer;
import com.example.BackEndSocial.repository.UserRepository;
import com.example.BackEndSocial.service.FriendService;
import com.example.BackEndSocial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/friendships")
public class FriendController {
    @Autowired
    private FriendService friendshipService;
    @Autowired
    private FriendNotificationProducer friendshipNotificationProducer;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/add/{friendId}")
    public ResponseEntity<String> addFriend(@PathVariable Long friendId, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Optional<User> friend = userRepository.findById(friendId);

        if (friend.isPresent()) {
            Friendship friendship = friendshipService.addFriend(user, friend.get());
            if (friendship != null) {
                String message = user.getFullName() + "|đã gửi lời mời kết bạn đến|" + friend.get().getFullName() + "|" + friend.get().getEmail();
                friendshipNotificationProducer.sendFriendshipNotification(message);
                return ResponseEntity.ok("Đã gửi lời mời kết bạn!");
            }
        }
        return ResponseEntity.badRequest().body("Không thể gửi lời mời kết bạn!");
    }

    @PostMapping("/accept/{friendId}")
    public ResponseEntity<String> acceptFriend(@PathVariable Long friendId, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Optional<User> friend = userRepository.findById(friendId);

        if (friend.isPresent()) {
            boolean accepted = friendshipService.acceptFriend(user, friend.get());
            if (accepted) {
                String message = user.getFullName() + "|đã chấp nhận lời mời kết bạn từ|" + friend.get().getFullName() + "|" + friend.get().getEmail();
                friendshipNotificationProducer.sendFriendshipNotification(message);
                return ResponseEntity.ok("Kết bạn thành công!");
            }
        }
        return new ResponseEntity<>("Không thể accept", HttpStatus.OK);
    }

    @PostMapping("/unfriend/{friendId}")
    public ResponseEntity<String> unfriend(@PathVariable Long friendId, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Optional<User> friend = userRepository.findById(friendId);

        if (friend.isPresent()) {
            boolean unfriended = friendshipService.unfriend(user, friend.get());
            if (unfriended) {
                String message = user.getFullName() + "|đã hủy kết bạn với|" + friend.get().getFullName() + "|" + friend.get().getEmail();
                // Gửi thẳng qua WebSocket
                messagingTemplate.convertAndSend("/topic/notifications", message);
                return ResponseEntity.ok("Đã hủy kết bạn!");
            }
        }
        return ResponseEntity.badRequest().body("Không thể hủy kết bạn!");
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchFriendsByName(
            @RequestHeader("Authorization") String jwt,
            @RequestParam String name) throws Exception {
        User user= userService.findUserByJwtToken(jwt);
        List<User> friends = friendshipService.searchFriendsByFullName(user.getId(), name);
        return ResponseEntity.ok(friends);
    }
}
