package com.capstone1.services.impl;

import java.util.List;
import org.springframework.stereotype.Service;

import com.capstone1.model.Staff;
import com.capstone1.repository.StaffRepository;
import com.capstone1.services.StaffService;

@Service
public class Staffimpl implements StaffService {

    private StaffRepository staffRepository;

    public Staffimpl(StaffRepository staffRepository) {
        super();
        this.staffRepository = staffRepository;
    }

    @Override
    public List<Staff> getAllStaffs() {
        return staffRepository.findAll();
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

}
