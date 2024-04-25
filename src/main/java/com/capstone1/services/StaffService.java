package com.capstone1.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.capstone1.model.Staff;

public interface StaffService {

    Page<Staff> getAllStaffs(Pageable p);

    Staff findByUserName(String username);

    Staff saveStaff(Staff staff);

    Staff updateStaff(Staff staff);

    Staff getStaffById(Long id);

    Staff findByEmail(String email);

    Staff changeStatusStaff(Staff staff);

    void deleteStaffById(Long id);

    Staff findByUsernameAndPassword(String usename, String passsword);

}
