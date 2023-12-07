package com.capstone1.services;

import java.util.List;

import com.capstone1.model.Staff;

public interface StaffService {

    List<Staff> getAllStaffs();

    Staff saveStaff(Staff staff);

    Staff updateStaff(Staff staff);

    Staff getStaffById(Long id);

    Staff findByEmail(String email);

    Staff changeStatusStaff(Staff staff);

    void deleteStaffById(Long id);

    Staff findByUsernameAndPassword(String usename, String passsword);

}
