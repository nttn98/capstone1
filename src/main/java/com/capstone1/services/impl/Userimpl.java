package com.capstone1.services.impl;

import java.util.List;

import org.springframework.stereotype.*;

import com.capstone1.model.User;
import com.capstone1.repository.UserRepository;
import com.capstone1.services.UserService;

@Service
public class Userimpl implements UserService {

    private UserRepository userRepository;

    public Userimpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User saveUser(User user) {
        userRepository.alterAutoIncrementValue();
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User changeStatusUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByUserEmail(email);
    }

}
