package com.example.BackEndSocial.repository;

import com.example.BackEndSocial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByEmail(String username);
}

