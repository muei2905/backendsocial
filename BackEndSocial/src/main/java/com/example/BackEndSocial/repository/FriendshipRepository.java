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

    @Query(value = """
    SELECT u.* FROM friendship f
    JOIN user u ON (
        (f.user_id = :userId AND f.friend_id = u.id)
        OR (f.friend_id = :userId AND f.user_id = u.id)
    )
    WHERE f.status = 'ACCEPTED'
    AND LOWER(u.full_name) LIKE LOWER(CONCAT('%', :name, '%'))
    """, nativeQuery = true)
    List<User> findFriendsByFullName(@Param("userId") Long userId, @Param("name") String name);

    @Query(value = """
    SELECT u.* FROM friendship f
    JOIN user u ON (
        (f.user_id = :userId AND f.friend_id = u.id)
        OR (f.friend_id = :userId AND f.user_id = u.id)
    )
    WHERE f.status = 'ACCEPTED'
    """, nativeQuery = true)
    List<User> findAllFriendsByUserId(@Param("userId") Long userId);

    @Query(value = """
    SELECT u.* FROM friendship f
    JOIN user u ON f.friend_id = u.id
    WHERE f.user_id = :userId AND f.status = 'PENDING'
    """, nativeQuery = true)
    List<User> findSentFriendRequests(@Param("userId") Long userId);

    @Query(value = """
    SELECT u.* FROM friendship f
    JOIN user u ON f.user_id = u.id
    WHERE f.friend_id = :userId AND f.status = 'PENDING'
    """, nativeQuery = true)
    List<User> findReceivedFriendRequests(@Param("userId") Long userId);
}
