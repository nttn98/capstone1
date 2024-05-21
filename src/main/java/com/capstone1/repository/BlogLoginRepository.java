package com.capstone1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.capstone1.model.BlogLogin;
import java.util.List;

public interface BlogLoginRepository extends JpaRepository<BlogLogin, Long> {

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE blog_login AUTO_INCREMENT = 1001", nativeQuery = true)
    void alterAutoIncrementValue();

    List<BlogLogin> findById(long id);
}
