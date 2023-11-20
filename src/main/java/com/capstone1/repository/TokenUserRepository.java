package com.capstone1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.capstone1.model.TokenUser;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

public interface TokenUserRepository extends JpaRepository<TokenUser, Long> {

    TokenUser findByToken(String token);

    void deleteByUserId(long user_id);

    void deleteByExpirationTimeBefore(LocalDateTime expirationTime);

}