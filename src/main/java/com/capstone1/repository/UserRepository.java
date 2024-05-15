package com.capstone1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.capstone1.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByUsername(String username);

    User findByGgID(String ggId);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE users AUTO_INCREMENT = 1001 ", nativeQuery = true)
    void alterAutoIncrementValue();

    User findByUsernameAndPassword(String username, String password);

    @Query("SELECT u.email FROM User u")
    List<String> findAllEmails();
}
