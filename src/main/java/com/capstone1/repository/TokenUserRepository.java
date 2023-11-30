package com.capstone1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.capstone1.model.TokenUser;

import java.time.LocalDateTime;

public interface TokenUserRepository extends JpaRepository<TokenUser, Long> {

    TokenUser findByToken(String token);

    @Transactional
    void deleteByUserId(long user_id);

    void deleteByExpirationTimeBefore(LocalDateTime expirationTime);

}