package com.capstone1.repository;

import org.springframework.data.jpa.repository.*;

import com.capstone1.model.Category;

import jakarta.transaction.Transactional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE categories AUTO_INCREMENT = 1001 ", nativeQuery = true)
    void alterAutoIncrementValue();
}
