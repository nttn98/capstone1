package com.capstone1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone1.model.Token;

import java.time.LocalDateTime;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findByToken(String token);

    void deleteByUserId(long user_id);

    void deleteByExpirationTimeBefore(LocalDateTime expirationTime);

}