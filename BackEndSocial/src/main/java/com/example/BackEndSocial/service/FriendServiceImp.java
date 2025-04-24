package com.example.BackEndSocial.service;

import com.example.BackEndSocial.model.Friendship;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class FriendServiceImp implements FriendService{
    @Autowired
    private FriendshipRepository friendshipRepository;

    @Override
    public Friendship addFriend(User user, User friend) {
        Friendship friendship = new Friendship(null, user, friend, "PENDING", LocalDateTime.now(), LocalDateTime.now());
        return friendshipRepository.save(friendship);
    }
    public List<Long> getFriendIds(Long userId) {
        return friendshipRepository.findFriendIdsByUserId(userId);
    }
    @Override
    public List<User> getAllFriends(Long userId) {
        return friendshipRepository.findAllFriendsByUserId(userId);
    }

    @Override
    public List<User> getSentFriendRequests(Long userId) {
        return friendshipRepository.findSentFriendRequests(userId);
    }

    @Override
    public List<User> getReceivedFriendRequests(Long userId) {
        return friendshipRepository.findReceivedFriendRequests(userId);
    }
    @Override
    public boolean acceptFriend(User user, User friend) {
        Optional<Friendship> friendship = friendshipRepository.findByUserAndFriend(user, friend);

        // Kiểm tra cả trường hợp friend gửi lời mời trước
        if (friendship.isEmpty()) {
            friendship = friendshipRepository.findByUserAndFriend(friend, user);
        }

        if (friendship.isPresent() && "PENDING".equals(friendship.get().getStatus())) {
            friendship.get().setStatus("ACCEPTED");
            friendship.get().setUpdateAt(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            friendshipRepository.save(friendship.get());
            return true;
        }

        return false;
    }
    @Override
    public List<User> searchFriendsByFullName(Long userId, String name) {
        String normalizedName = normalize(name); // bỏ dấu
        return friendshipRepository.findFriendsByFullName(userId, normalizedName);
    }

    @Override
    public boolean unfriend(User user, User friend) {
        Optional<Friendship> friendship = friendshipRepository.findByUserAndFriend(user, friend);
        if (friendship.isPresent() && "ACCEPTED".equals(friendship.get().getStatus())) {
            friendshipRepository.delete(friendship.get());
            return true;
        }
        return false;
    }

    public static String normalize(String input) {
        if (input == null) return null;
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "") // bỏ dấu
                .replaceAll("[đĐ]", "d")   // chuyển đ -> d
                .toLowerCase();            // chuyển về chữ thường
    }

}
