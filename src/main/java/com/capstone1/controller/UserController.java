package com.capstone1.controller;

import java.util.*;

import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import com.capstone1.model.User;
import com.capstone1.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    private UserService userService;
    private Encoding encoding;
    private HomeController homeController;

    public UserController(UserService userService, Encoding encoding, HomeController homeController) {
        super();
        this.userService = userService;
        this.encoding = encoding;
        this.homeController = homeController;
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

    @GetMapping("/users/create-user")
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

    @PostMapping("/users/update-user")
    public String updateUser(Model model, @ModelAttribute("user") User user, @RequestParam("mode") String mode,
            HttpSession session, @RequestParam("userId") Long userId) {
        User existUser = userService.getUserById(userId);

        existUser.setUserFullname(user.getUserFullname());
        existUser.setUserNumberphone(user.getUserNumberphone());
        existUser.setUserAddress(user.getUserAddress());
        existUser.setUserEmail(user.getUserEmail());
        existUser.setUserDob(user.getUserDob());

        userService.updateUser(existUser);
        System.out.println("User edited successfully");

        if (mode.equals("user")) {
            session.setAttribute("user", existUser);
            model.addAttribute("alert", "edit");
            return homeController.getHome(model);
        } else {
            return "redirect:/users";
        }
    }

    @GetMapping("/users/change-status/{id}")
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

    @GetMapping("/users/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/users";
    }

    @PostMapping("/users/save-user")
    public String saveUser(Model model, @ModelAttribute("user") User user) {
        user.setUserPassword(encoding.toSHA1(user.getUserPassword()));
        userService.saveUser(user);
        System.out.println("User added successfully");
        return "redirect:/users";
    }

    /* Change password */
    @GetMapping("/users/toChangePass/{id}")
    public String changePass(@PathVariable Long id, Model model, @ModelAttribute("user") User user) {
        User existUser = userService.getUserById(id);
        System.out.println("--------------------" + existUser.getUserPassword());
        model.addAttribute("user", existUser);
        return "users/changePass_user";
    }

    @PostMapping("users/do-change-pass/{id}")
    public String changePasswod(@PathVariable Long id, Model model, @ModelAttribute("user") User user,
            @RequestParam("oldPassword") String oldPass, @RequestParam("newPassword") String newPass) {
        User existUser = userService.getUserById(id);
        String oldUserPass = existUser.getUserPassword();

        String oldPassword = encoding.toSHA1(oldPass);

        if (oldUserPass.equals(oldPassword)) {
            existUser.setUserPassword(newPass);
            saveUser(model, existUser);
            System.out.println("---------------------Success " + existUser.getUserPassword());
        } else {
            System.out.println("---------------------Fail");
        }

        return "redirect:/users";

    }
}
