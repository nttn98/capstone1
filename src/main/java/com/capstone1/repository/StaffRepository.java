package com.capstone1.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import com.capstone1.model.Staff;

import jakarta.transaction.Transactional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE staffs AUTO_INCREMENT = 1001", nativeQuery = true)
    void alterAutoIncrementValue();

}
