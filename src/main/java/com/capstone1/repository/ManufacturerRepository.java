package com.capstone1.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import com.capstone1.model.Manufacturer;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE manufacturers AUTO_INCREMENT = 1001 ", nativeQuery = true)
    void alterAutoIncrementValue();
}
