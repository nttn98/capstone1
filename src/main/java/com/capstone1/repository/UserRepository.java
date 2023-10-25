package com.capstone1.repository;

import org.springframework.data.jpa.repository.*;

import com.capstone1.model.User;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE users AUTO_INCREMENT = 1001 ", nativeQuery = true)
    void alterAutoIncrementValue();

}
