package com.capstone1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email AND u.email <> :originalEmail")
    boolean existsByEmail(@Param("email") String email, @Param("originalEmail") String originalEmail);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username")
    boolean existsByUsername(@Param("username") String username);
}
