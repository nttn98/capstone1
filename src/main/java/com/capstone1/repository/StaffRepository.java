package com.capstone1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.capstone1.model.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    Staff findByEmail(String email);

    Staff findByUsernameAndPassword(String username, String password);

    Staff findByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE staffs AUTO_INCREMENT = 1001", nativeQuery = true)
    void alterAutoIncrementValue();

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Staff s WHERE s.email = :email AND s.email <> :originalEmail")
    boolean existsByEmail(@Param("email") String email, @Param("originalEmail") String originalEmail);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Staff s WHERE s.username = :username")
    boolean existsByUsername(@Param("username") String username);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Staff s WHERE s.idcard = :idcard")
    boolean existsByIdcard(@Param("idcard") long idcard);
}
