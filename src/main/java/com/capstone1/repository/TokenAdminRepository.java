package com.capstone1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.capstone1.model.TokenAdmin;

import java.time.LocalDateTime;

public interface TokenAdminRepository extends JpaRepository<TokenAdmin, Long> {

    @Modifying
    @Transactional
    // @Query(value = "ALTER TABLE token_admins AUTO_INCREMENT = 1001", nativeQuery
    // = true)
    @Query(value = "DBCC CHECKIDENT('dbo.token_admins', RESEED, 1001)", nativeQuery = true)

    void alterAutoIncrementValue();

    TokenAdmin findByToken(String token);

    @Transactional
    void deleteByStaffId(long staff_id);

    void deleteByExpirationTimeBefore(LocalDateTime expirationTime);

}