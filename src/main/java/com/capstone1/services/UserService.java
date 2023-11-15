package com.capstone1.services;

import java.util.*;

import com.capstone1.model.User;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    User saveUser(User user);

    User updateUser(User user);

    User changeStatusUser(User user);

    void deleteUserById(Long id);

    User getUserByEmail(String email);

}
