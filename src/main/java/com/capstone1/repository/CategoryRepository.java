package com.capstone1.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import com.capstone1.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE categories AUTO_INCREMENT = 1001 ", nativeQuery = true)
    // @Query(value = "DBCC CHECKIDENT('dbo.categories', RESEED, 1003);",
    // nativeQuery = true)
    void alterAutoIncrementValue();

}
