package com.capstone1.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.capstone1.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

    @Query(value = "SELECT c.id FROM Category c WHERE c.name = :name")
    Long findIdByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE categories AUTO_INCREMENT = 1001 ", nativeQuery = true)
    void alterAutoIncrementValue();

}
