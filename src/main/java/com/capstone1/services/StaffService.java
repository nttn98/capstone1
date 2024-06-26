package com.capstone1.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.capstone1.model.Staff;

public interface StaffService {

    List<Staff> getAll();

    Page<Staff> getAllStaffs(Pageable p);

    Staff findByUserName(String username);

    Staff saveStaff(Staff staff);

    Staff updateStaff(Staff staff);

    Staff getStaffById(Long id);

    Staff findByEmail(String email);

    Staff changeStatusStaff(Staff staff);

    void deleteStaffById(Long id);

    Staff findByUsernameAndPassword(String usename, String passsword);

    boolean checkEmail(String email, String originalEmail);

    boolean checkUsername(String username);

    boolean checkIdcard(long idcard);

}
