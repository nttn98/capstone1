package com.capstone1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.capstone1.model.Admin;
import com.capstone1.model.Staff;
import com.capstone1.model.User;
import com.capstone1.services.AdminService;
import com.capstone1.services.CommonService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    @Resource
    AdminService adminService;
    @Resource
    CommonService commonService;

    @Autowired
    HomeController homeController;
    @Autowired
    private Encoding encoding;

    @PostMapping("admins/do-change-pass")
    public String changePasswod(@RequestParam Long id, Model model, @ModelAttribute Admin admin,
            @RequestParam("oldPassword") String oldPass, @RequestParam("newPassword") String newPass,
            HttpSession session) {
        Admin existAdmin = adminService.getAdminById(id);
        String oldUserPass = existAdmin.getPassword();

        String oldPassword = encoding.toSHA1(oldPass);

        if (oldUserPass.equals(oldPassword)) {
            existAdmin.setPassword(newPass);
            updateAdmin(model, existAdmin, session);
            System.out.println("---------------------Success " + existAdmin.getPassword());
            model.addAttribute("alert", "changePass");
            session.removeAttribute("admin");
        } else {
            model.addAttribute("alert", "error");
            System.out.println("---------------------Fail");
        }
        return homeController.getDashBoardPage(model, session);
    }

    @PostMapping("/admins/admin-staff")
    public String updateAdmin(Model model, @ModelAttribute Admin admin, HttpSession session) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        admin.setPassword(encoding.toSHA1(admin.getPassword()));
        adminService.saveAdmin(admin);
        System.out.println("Admin update successfully");
        model.addAttribute("alert", "successRegister");
        model.getAttribute("admin");
        return null;
    }
}
