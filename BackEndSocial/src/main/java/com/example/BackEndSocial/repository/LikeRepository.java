package com.example.BackEndSocial.repository;

import com.example.BackEndSocial.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<PostLike, Long> {
}
