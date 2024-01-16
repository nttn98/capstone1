package com.capstone1.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import com.capstone1.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Modifying
    @Transactional
    // @Query(value = "ALTER TABLE dbo.users AUTO_INCREMENT = 1001 ", nativeQuery =
    // true)
    @Query(value = "DBCC CHECKIDENT('dbo.users', RESEED, 1001)", nativeQuery = true)
    void alterAutoIncrementValue();

    User findByUsernameAndPassword(String username, String password);
}
