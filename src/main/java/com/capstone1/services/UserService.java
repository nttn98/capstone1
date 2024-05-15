package com.capstone1.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.capstone1.model.User;

public interface UserService {

    List<User> getAll();

    Page<User> getAllUsers(Pageable p);

    User getUserById(Long id);

    User saveUser(User user);

    User updateUser(User user);

    User changeStatusUser(User user);

    void deleteUserById(Long id);

    User findByEmail(String email);

    User findByUsername(String username);

    User findByGgID(String id);

    User findByUsernameAndPassword(String usename, String passsword);

    List<String> getAllEmails();
}
