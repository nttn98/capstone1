package com.capstone1.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;

import com.capstone1.model.Staff;
import com.capstone1.services.StaffService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;

@Controller
public class StaffController {

    @Resource
    StaffService staffService;

    @Autowired
    Encoding encoding;
    @Autowired
    HomeController homeController;

    @GetMapping("/staffs")
    public String listStaffs(Model model, HttpSession session) {
        homeController.isLogin(model, session);
        List<Staff> listStaffs = staffService.getAllStaffs();

        if (listStaffs.size() == 0) {
            Staff staff = new Staff();
            model.addAttribute("staff", staff);
            return "staffs/create_staff";
        } else {
            model.addAttribute("staffs", listStaffs);
            return "staffs/staffs";
        }
    }

    @GetMapping("/staffs/create-staff")
    public String createStaff(Model model) {
        Staff staff = new Staff();
        model.addAttribute("staff", staff);
        return "staffs/create_staff";
    }

    @GetMapping("/staffs/edit/{id}")
    public String editStaff(@PathVariable Long id, Model model) {
        Staff staff = staffService.getStaffById(id);
        model.addAttribute("staff", staff);
        return "staffs/edit_staff";
    }

    @PostMapping("/staffs/update-staff")
    public String updateStaff(@RequestParam long id, Model model, @ModelAttribute("staff") Staff staff,
            HttpSession session) {

        if (session == null) {
            return "loginAdmin_Staff";
        }

        Staff existStaff = staffService.getStaffById(id);

        existStaff.setFullname(staff.getFullname());
        existStaff.setNumberphone(staff.getNumberphone());
        existStaff.setIdcard(staff.getIdcard());
        existStaff.setEmail(staff.getEmail());
        existStaff.setDob(staff.getDob());

        staffService.updateStaff(existStaff);
        System.out.println("Staff edited successfully");
        model.addAttribute("alert", "edit");

        return listStaffs(model, session);
    }

    @GetMapping("/staffs/change-status/{id}")
    public String changeStatus(@PathVariable Long id, Model model, @ModelAttribute("staff") Staff staff) {
        Staff existStaff = staffService.getStaffById(id);

        if (existStaff.getStatus() == 0) {
            existStaff.setStatus(1);
        } else {
            existStaff.setStatus(0);
        }

        staffService.updateStaff(existStaff);
        return "redirect:/staffs";
    }

    @GetMapping("/staffs/toChangePass/{id}")
    public String changePass(@PathVariable Long id, Model model, @ModelAttribute("staff") Staff staff) {
        Staff existStaff = staffService.getStaffById(id);
        System.out.println("--------------------" + existStaff.getPassword());
        model.addAttribute("staff", existStaff);
        return "staffs/changePass_staff";
    }

    @PostMapping("/staffs/do-change-pass")
    public String changePassword(@RequestParam("id") Long id, Model model, @ModelAttribute("staff") Staff staff,
            @RequestParam("oldPassword") String oldPass, @RequestParam("newPassword") String newPass,
            HttpSession session) {

        Staff existStaff = staffService.getStaffById(id);
        String oldStaffPass = existStaff.getPassword();

        String oldPassword = encoding.toSHA1(oldPass);

        if (oldStaffPass.equals(oldPassword)) {
            existStaff.setPassword(newPass);
            saveStaff(model, existStaff, session);
            System.out.println("---------------------Success " + existStaff.getPassword());
            model.addAttribute("alert", "success");
        } else {
            System.out.println("---------------------Fail");
            model.addAttribute("alert", "error");
        }
        homeController.isLogin(model, session);
        return homeController.getDashBoardPage(model, session);
    }

    @GetMapping("/staffs/delete-staff/{id}")
    public String deleteStaff(@PathVariable Long id) {
        staffService.deleteStaffById(id);
        return "redirect:/staffs";
    }

    @PostMapping("/staffs/save-staff")
    public String saveStaff(Model model, @ModelAttribute("staff") Staff staff, HttpSession session) {
        staff.setPassword(encoding.toSHA1(staff.getPassword()));
        staffService.saveStaff(staff);
        System.out.println("Staff added successfully");
        model.addAttribute("alert", "successRegister");

        return listStaffs(model, session);
    }

}
