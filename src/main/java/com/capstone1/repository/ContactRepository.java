package com.capstone1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.capstone1.model.Contact;

import jakarta.transaction.Transactional;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE contacts AUTO_INCREMENT = 1001 ", nativeQuery = true)
    void alterAutoIncrementValue();
}
