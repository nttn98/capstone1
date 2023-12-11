package com.capstone1.services;

import com.capstone1.model.Admin;

public interface AdminService {

    Admin findByUsernameAndPassword(String usename, String passsword);

}
