package com.capstone1.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import com.capstone1.model.User;
import com.capstone1.services.UserService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Resource
    UserService userService;
    @Autowired
    HomeController homeController;
    @Autowired
    private Encoding encoding;

    @GetMapping("/users")
    public String listUsers(Model model, HttpSession session) {
        List<User> listUsers = userService.getAllUsers();

        Boolean flag = homeController.checkLogin(model, session);
        if (flag == false) {
            return homeController.getLoginPage(model);
        }

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
            HttpSession session, @RequestParam("id") Long userId) {
        User existUser = userService.getUserById(userId);

        existUser.setFullname(user.getFullname());
        existUser.setNumberphone(user.getNumberphone());
        existUser.setAddress(user.getAddress());
        existUser.setEmail(user.getEmail());
        existUser.setDob(user.getDob());

        userService.updateUser(existUser);
        System.out.println("User edited successfully");

        if (mode.equals("user")) {
            session.setAttribute("user", existUser);
            model.addAttribute("alert", "edit");
            return homeController.getHome(model, session);
        } else {
            return "redirect:/users";
        }
    }

    @GetMapping("/users/change-status/{id}")
    public String changeStatus(@PathVariable Long id, Model model, @ModelAttribute("user") User user) {
        User existUser = userService.getUserById(id);

        if (existUser.getStatus() == 0) {
            existUser.setStatus(1);
        } else {
            existUser.setStatus(0);
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
    public String saveUser(Model model, @ModelAttribute("user") User user, @RequestParam("mode") String mode,
            HttpSession session) {

        user.setPassword(encoding.toSHA1(user.getPassword()));
        userService.saveUser(user);
        System.out.println("User added successfully");
        model.addAttribute("alert", "successRegister");
        if (mode.equals("user")) {
            return homeController.getHome(model, session);
        } else {
            return "redirect:/users";
        }
    }

    /* Change password */
    @GetMapping("/users/to-change-pass/{id}")
    public String changePass(@PathVariable Long id, Model model, @ModelAttribute("user") User user) {
        User existUser = userService.getUserById(id);
        System.out.println("--------------------" + existUser.getPassword());
        model.addAttribute("user", existUser);
        return "users/changePass_user";
    }

    @PostMapping("users/do-change-pass")
    public String changePasswod(@RequestParam Long id, Model model, @ModelAttribute("user") User user,
            @RequestParam("oldPassword") String oldPass, @RequestParam("newPassword") String newPass,
            HttpSession session, @RequestParam("mode") String mode) {
        User existUser = userService.getUserById(id);
        String oldUserPass = existUser.getPassword();

        String oldPassword = encoding.toSHA1(oldPass);

        if (oldUserPass.equals(oldPassword)) {
            existUser.setPassword(newPass);
            saveUser(model, existUser, mode, session);
            System.out.println("---------------------Success " + existUser.getPassword());
            model.addAttribute("alert", "changePass");

        } else {
            model.addAttribute("alert", "error");
            System.out.println("---------------------Fail");
        }

        if (mode.equals("user")) {
            return homeController.getHome(model, session);
        } else {
            return "redirect:/users";
        }

    }
}
