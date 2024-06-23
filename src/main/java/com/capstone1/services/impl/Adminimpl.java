package com.capstone1.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone1.model.Admin;
import com.capstone1.repository.AdminRepository;
import com.capstone1.services.AdminService;

@Service
public class Adminimpl implements AdminService {

    @Autowired
    AdminRepository adminRepository;

    @Override
    public Admin findByUsernameAndPassword(String usename, String passsword) {
        return adminRepository.findByUsernameAndPassword(usename, passsword);
    }

    @Override
    public Admin saveAdmin(Admin admin) {
        adminRepository.alterAutoIncrementValue();
        return adminRepository.save(admin);
    }

    @Override
    public Admin updateAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public Admin getAdminById(Long id) {
        return adminRepository.findById(id).get();
    }

    @Override
    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

}
