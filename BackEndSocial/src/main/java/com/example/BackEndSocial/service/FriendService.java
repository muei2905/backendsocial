package com.example.BackEndSocial.service;

import com.example.BackEndSocial.model.Friendship;
import com.example.BackEndSocial.model.User;

public interface FriendService {
    Friendship addFriend(User user, User friend);
    boolean acceptFriend(User user, User friend);
    boolean unfriend(User user, User friend);
}
