package com.example.BackEndSocial.service;

import com.example.BackEndSocial.model.Friendship;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Override
    public boolean acceptFriend(User user, User friend) {
        Optional<Friendship> friendship = friendshipRepository.findByUserAndFriend(user, friend);

        // Kiểm tra cả trường hợp friend gửi lời mời trước
        if (friendship.isEmpty()) {
            friendship = friendshipRepository.findByUserAndFriend(friend, user);
        }

        if (friendship.isPresent() && "PENDING".equals(friendship.get().getStatus())) {
            friendship.get().setStatus("ACCEPTED");
            friendship.get().setUpdateAt(LocalDateTime.now());
            friendshipRepository.save(friendship.get());
            return true;
        }

        return false;
    }


    @Override
    public boolean unfriend(User user, User friend) {
        Optional<Friendship> friendship = friendshipRepository.findByUserAndFriend(user, friend);
        if (friendship.isPresent() && "ACCEPTED".equals(friendship.get().getStatus())) {
            friendship.get().setStatus("UNFRIENDED");
            friendship.get().setUpdateAt(LocalDateTime.now());
            friendshipRepository.save(friendship.get());
            return true;
        }
        return false;
    }
}
