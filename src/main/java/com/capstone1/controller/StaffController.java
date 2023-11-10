package com.capstone1.controller;

import java.util.*;

import org.springframework.stereotype.*;
import org.springframework.ui.Model;

import com.capstone1.model.Staff;
import com.capstone1.services.StaffService;

import org.springframework.web.bind.annotation.*;

@Controller
public class StaffController {

    private StaffService staffService;
    private Encoding encoding;

    public StaffController(StaffService staffService, Encoding encoding) {
        super();
        this.staffService = staffService;
        this.encoding = encoding;
    }

    @GetMapping(value = "/staffs")
    public String listStaffs(Model model) {
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

    @GetMapping("/staffs/createStaff")
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

    @PostMapping("staffs/updateStaff/{id}")
    public String updateStaff(@PathVariable Long id, Model model, @ModelAttribute("staff") Staff staff) {
        Staff existStaff = staffService.getStaffById(id);

        existStaff.setStaffFullname(staff.getStaffFullname());
        existStaff.setStaffUsername(staff.getStaffUsername());
        existStaff.setStaffNumberphone(staff.getStaffNumberphone());
        existStaff.setStaffIdcard(staff.getStaffIdcard());
        existStaff.setStaffEmail(staff.getStaffEmail());
        existStaff.setStaffDob(staff.getStaffDob());

        staffService.updateStaff(existStaff);
        System.out.println("Staff edited successfully");

        return "redirect:/staffs";
    }

    @GetMapping("/staffs/changeStatus/{id}")
    public String changeStatus(@PathVariable Long id, Model model, @ModelAttribute("staff") Staff staff) {
        Staff existStaff = staffService.getStaffById(id);

        if (existStaff.getStaffStatus() == 0) {
            existStaff.setStaffStatus(1);
        } else {
            existStaff.setStaffStatus(0);
        }

        staffService.updateStaff(existStaff);
        return "redirect:/staffs";
    }

    @GetMapping("/staffs/toChangePass/{id}")
    public String changePass(@PathVariable Long id, Model model, @ModelAttribute("staff") Staff staff) {
        model.addAttribute("staff", staffService.getStaffById(id));
        return "staffs/changePass_staff";
    }

    @PostMapping("/staffs/doChangePass/{id}")
    public String changePassword(@PathVariable Long id, Model model, @ModelAttribute("staff") Staff staff,
            @RequestParam("oldPassword") String pass) {
        String newPass = encoding.toSHA1(pass);

        if (staff.getStaffPassword().equals(newPass)) {
            staff.setStaffPassword(newPass);
            updateStaff(id, model, staff);
        } else {
            System.out.println("------------------false");
        }
        return "redirect:/staffs";
    }

    @GetMapping("/staffs/deleteStaff/{id}")
    public String deleteStaff(@PathVariable Long id) {
        staffService.deleteStaffById(id);
        return "redirect:/staffs";
    }

    @PostMapping("/staffs/saveStaff")
    public String saveStaff(Model model, @ModelAttribute("staff") Staff staff) {
        staff.setStaffPassword(encoding.toSHA1(staff.getStaffPassword()));
        staffService.saveStaff(staff);
        System.out.println("Staff added successfully");
        return "redirect:/staffs";
    }
}
