package com.capstone1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.capstone1.model.Staff;
import com.capstone1.services.CommonService;
import com.capstone1.services.StaffService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

@Controller
public class StaffController {

    @Resource
    StaffService staffService;
    @Resource
    CommonService commonService;

    @Autowired
    Encoding encoding;
    @Autowired
    ProductController productController;

    @GetMapping("/staffs")
    public String listStaffs(Model model, HttpSession session, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String target = commonService.isLogin(model, session);
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
    public String createStaff(Model model, HttpSession session) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        Staff staff = new Staff();
        model.addAttribute("staff", staff);
        return "staffs/create_staff";
    }

    @GetMapping("/staffs/edit/{id}")
    public String editStaff(@PathVariable Long id, Model model, HttpSession session) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        Staff staff = staffService.getStaffById(id);
        model.addAttribute("staff", staff);
        return "staffs/edit_staff";
    }

    @PostMapping("/staffs/update-staff")
    public String updateStaff(@RequestParam long id, Model model, @ModelAttribute Staff staff,
            HttpSession session, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, @RequestParam String email,
            @RequestParam("mode") String mode) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }

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

        if (mode.equals("staff")) {
            session.setAttribute("staff", existStaff);
            return productController.listProducts(model, session);
        }
        return listStaffs(model, session, page, size);
    }

    @GetMapping("/staffs/change-status/{id}")
    public String changeStatus(@PathVariable Long id, Model model, @ModelAttribute Staff staff, HttpSession session,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
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
    public String changePass(@PathVariable Long id, Model model, @ModelAttribute Staff staff, HttpSession session) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
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
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
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
        commonService.isLogin(model, session);
        return productController.listProducts(model, session);
    }

    @GetMapping("/staffs/delete-staff/{id}")
    public String deleteStaff(@PathVariable Long id, Model model, HttpSession session) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        staffService.deleteStaffById(id);
        return "redirect:/staffs";
    }

    @PostMapping("/staffs/save-staff")
    public String saveStaff(Model model, @ModelAttribute Staff staff, HttpSession session,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        staff.setPassword(encoding.toSHA1(staff.getPassword()));
        staff.setStatus(1);
        staffService.saveStaff(staff);
        System.out.println("Staff added successfully");
        model.addAttribute("alert", "successRegister");
        model.getAttribute("admin");
        return listStaffs(model, session, page, size);
    }

}
