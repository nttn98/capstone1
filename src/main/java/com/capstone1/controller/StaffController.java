package com.capstone1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.capstone1.model.Staff;
import com.capstone1.services.StaffService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

@Controller
public class StaffController {

    @Resource
    StaffService staffService;

    @Autowired
    Encoding encoding;
    @Autowired
    HomeController homeController;

    @GetMapping("/staffs")
    public String listStaffs(Model model, HttpSession session, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String target = homeController.isLogin(model, session);
        if (target != null) {
            return target;
        }
        List<Staff> listStaffs = staffService.getAll();

        if (listStaffs.isEmpty()) {
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
    public String updateStaff(@RequestParam long id, Model model, @ModelAttribute Staff staff,
            HttpSession session, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

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

        return listStaffs(model, session, page, size);
    }

    @GetMapping("/staffs/change-status/{id}")
    public String changeStatus(@PathVariable Long id, Model model, @ModelAttribute Staff staff, HttpSession session,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Staff existStaff = staffService.getStaffById(id);

        if (existStaff.getStatus() == 0) {
            existStaff.setStatus(1);
        } else {
            existStaff.setStatus(0);
        }
        model.addAttribute("alert", "edit");

        staffService.updateStaff(existStaff);
        return listStaffs(model, session, page, size);
    }

    @GetMapping("/staffs/toChangePass/{id}")
    public String changePass(@PathVariable Long id, Model model, @ModelAttribute Staff staff) {
        Staff existStaff = staffService.getStaffById(id);
        System.out.println("--------------------" + existStaff.getPassword());
        model.addAttribute("staff", existStaff);
        return "staffs/changePass_staff";
    }

    @PostMapping("/staffs/do-change-pass")
    public String changePassword(@RequestParam Long id, Model model, @ModelAttribute Staff staff,
            @RequestParam("oldPassword") String oldPass, @RequestParam("newPassword") String newPass,
            HttpSession session, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Staff existStaff = staffService.getStaffById(id);
        String oldStaffPass = existStaff.getPassword();

        String oldPassword = encoding.toSHA1(oldPass);

        if (oldStaffPass.equals(oldPassword)) {
            existStaff.setPassword(newPass);
            saveStaff(model, existStaff, session, page, size);
            System.out.println("---------------------Success " + existStaff.getPassword());
            model.addAttribute("alert", "success");
        } else {
            System.out.println("---------------------Fail");
            model.addAttribute("alert", "error");
        }
        homeController.isLogin(model, session);
        return homeController.getDashBoardPage(model, session, page, size);
    }

    @GetMapping("/staffs/delete-staff/{id}")
    public String deleteStaff(@PathVariable Long id) {
        staffService.deleteStaffById(id);
        return "redirect:/staffs";
    }

    @PostMapping("/staffs/save-staff")
    public String saveStaff(Model model, @ModelAttribute Staff staff, HttpSession session,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        staff.setPassword(encoding.toSHA1(staff.getPassword()));
        staffService.saveStaff(staff);
        System.out.println("Staff added successfully");
        model.addAttribute("alert", "successRegister");

        return listStaffs(model, session, page, size);
    }

    /* check name is unique */
    @GetMapping("/checkStaffUsernameAvailability")
    @ResponseBody // Ensure the returned boolean is serialized as a response body
    public ResponseEntity<Boolean> checkStaffUsernameAvailability(@RequestParam("name") String name) {
        try {
            Staff staffExist = staffService.findByUserName(name);
            if (staffExist == null) {
                return ResponseEntity.ok(true); // name is available
            } else {
                return ResponseEntity.ok(false); // name is not available
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

}
