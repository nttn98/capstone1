package com.capstone1.controller;

import java.util.*;

import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import com.capstone1.model.User;
import com.capstone1.services.UserService;

@Controller
public class UserController {

    private UserService userService;
    private Encoding encoding;

    public UserController(UserService userService, Encoding encoding) {
        super();
        this.userService = userService;
        this.encoding = encoding;
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = userService.getAllUsers();
        if (listUsers.size() == 0) {
            User user = new User();
            model.addAttribute("user", user);
            return "users/create_user";
        } else {
            model.addAttribute("users", listUsers);
            return "users/users";
        }

    }

    @GetMapping("/users/createUser")
    public String createUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "users/create_user";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "users/edit_user";
    }

    @PostMapping("/users/updateUser/{id}")
    public String updateUser(@PathVariable Long id, Model model,
            @ModelAttribute("user") User user) {
        User existUser = userService.getUserById(id);

        existUser.setUserFullname(user.getUserFullname());
        existUser.setUserUsername(user.getUserUsername());
        existUser.setUserNumberphone(user.getUserNumberphone());
        existUser.setUserAddress(user.getUserAddress());
        existUser.setUserEmail(user.getUserEmail());
        existUser.setUserDob(user.getUserDob());

        userService.updateUser(existUser);
        System.out.println("User edited successfully");

        return "redirect:/users";
    }

    @GetMapping("/users/changeStatus/{id}")
    public String changeStatus(@PathVariable Long id, Model model, @ModelAttribute("user") User user) {
        User existUser = userService.getUserById(id);

        if (existUser.getUserStatus() == 0) {
            existUser.setUserStatus(1);
        } else {
            existUser.setUserStatus(0);
        }

        userService.updateUser(existUser);
        return "redirect:/users";
    }

    @GetMapping("/users/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/users";
    }

    @PostMapping("/users/saveUser")
    public String saveUser(Model model, @ModelAttribute("user") User user) {
        user.setUserPassword(encoding.toSHA1(user.getUserPassword()));
        userService.saveUser(user);
        System.out.println("User added successfully");
        return "redirect:/users";
    }
}
