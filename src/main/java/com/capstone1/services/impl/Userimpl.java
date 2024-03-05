package com.capstone1.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<User> getAllUsers(Pageable p) {
        return userRepository.findAll(p);
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
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByUsernameAndPassword(String usename, String passsword) {
        return userRepository.findByUsernameAndPassword(usename, passsword);
    }

}
