package com.capstone1.services;

import com.capstone1.model.Admin;

public interface AdminService {

    Admin findByUsernameAndPassword(String usename, String passsword);

    Admin saveAdmin(Admin admin);

    Admin updateAdmin(Admin admin);

    Admin getAdminById(Long id);

}
