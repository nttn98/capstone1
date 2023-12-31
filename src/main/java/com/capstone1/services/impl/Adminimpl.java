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

}
