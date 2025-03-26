package com.example.BackEndSocial.repository;

import com.example.BackEndSocial.model.Friendship;
import com.example.BackEndSocial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    Optional<Friendship> findByUserAndFriend(User user, User friend);
}
