package com.capstone1.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.capstone1.model.Staff;
import com.capstone1.repository.StaffRepository;
import com.capstone1.services.StaffService;

@Service
public class Staffimpl implements StaffService {

    @Autowired
    StaffRepository staffRepository;

    @Override
    public Page<Staff> getAllStaffs(Pageable p) {
        return staffRepository.findAll(p);
    }

    @Override
    public Staff findByUserName(String username) {
        return staffRepository.findByUsername(username);
    }

    @Override
    public Staff saveStaff(Staff staff) {
        staffRepository.alterAutoIncrementValue();
        return staffRepository.save(staff);
    }

    @Override
    public Staff updateStaff(Staff staff) {
        return staffRepository.save(staff);
    }

    @Override
    public Staff getStaffById(Long id) {
        return staffRepository.findById(id).get();
    }

    @Override
    public Staff changeStatusStaff(Staff staff) {
        return staffRepository.save(staff);
    }

    @Override
    public void deleteStaffById(Long id) {
        staffRepository.deleteById(id);
    }

    @Override
    public Staff findByEmail(String email) {
        return staffRepository.findByEmail(email);
    }

    @Override
    public Staff findByUsernameAndPassword(String usename, String passsword) {
        return staffRepository.findByUsernameAndPassword(usename, passsword);
    }

    @Override
    public List<Staff> getAll() {
        return staffRepository.findAll();
    }

    @Override
    public boolean checkEmail(String email, String originalEmail) {
        return staffRepository.existsByEmail(email, originalEmail);

    }

    @Override
    public boolean checkUsername(String username) {
        return staffRepository.existsByUsername(username);
    }

    @Override
    public boolean checkIdcard(long idcard) {
        return staffRepository.existsByIdcard(idcard);
    }

}
