package com.example.BackEndSocial.service;

import com.example.BackEndSocial.model.Friendship;
import com.example.BackEndSocial.model.User;

import java.util.List;

public interface FriendService {
    Friendship addFriend(User user, User friend);
    boolean acceptFriend(User user, User friend);
    boolean unfriend(User user, User friend);
    List<Long> getFriendIds(Long userId);
    public List<User> searchFriendsByFullName(Long userId, String name);
    List<User> getAllFriends(Long userId);
    List<User> getSentFriendRequests(Long userId); // Lời mời đã gửi
    List<User> getReceivedFriendRequests(Long userId);
    public void cancelFriendRequest(Long userId, Long friendId);
}
