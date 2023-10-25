package com.capstone1.repository;

import org.springframework.data.jpa.repository.*;

import com.capstone1.model.*;

import jakarta.transaction.Transactional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE admins AUTO_INCREMENT = 1001 ", nativeQuery = true)
    void alterAutoIncrementValue();
}
