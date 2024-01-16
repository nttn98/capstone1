package com.capstone1.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.capstone1.model.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    Staff findByEmail(String email);

    Staff findByUsernameAndPassword(String username, String password);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE staffs AUTO_INCREMENT = 1001", nativeQuery = true)
    // @Query(value = "DBCC CHECKIDENT('dbo.staffs', RESEED, 1001)", nativeQuery =
    // true)

    void alterAutoIncrementValue();

}
