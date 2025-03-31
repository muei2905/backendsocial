package com.example.BackEndSocial.repository;

import com.example.BackEndSocial.model.Friendship;
import com.example.BackEndSocial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    Optional<Friendship> findByUserAndFriend(User user, User friend);
    @Query("SELECT f.friend.id FROM Friendship f WHERE f.user.id = :userId AND f.status = 'ACCEPTED' " +
            "UNION " +
            "SELECT f.user.id FROM Friendship f WHERE f.friend.id = :userId AND f.status = 'ACCEPTED'")
    List<Long> findFriendIdsByUserId(@Param("userId") Long userId);
}
