package com.capstone1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone1.model.TokenAdmin;

import java.time.LocalDateTime;

public interface TokenAdminRepository extends JpaRepository<TokenAdmin, Long> {

    TokenAdmin findByToken(String token);

    void deleteByStaffId(long staff_id);

    void deleteByExpirationTimeBefore(LocalDateTime expirationTime);

}