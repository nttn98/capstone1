package com.capstone1.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import com.capstone1.model.*;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findByUsernameAndPassword(String username, String password);

    // @Modifying
    // @Transactional
    // @Query(value = "ALTER TABLE admins AUTO_INCREMENT = 1001 ", nativeQuery = true)
    // // @Query(value = "DBCC CHECKIDENT('dbo.admins', RESEED, 1001)", nativeQuery =
    // // true)
    // void alterAutoIncrementValue();
}
