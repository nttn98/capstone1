package com.capstone1.services;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.capstone1.model.Admin;
import com.capstone1.model.Staff;
import com.capstone1.model.User;

import jakarta.servlet.http.HttpSession;

@Service
public class CommonService {

    public String getLoginPage(Model model, HttpSession session) {
        session.removeAttribute("staff");
        session.removeAttribute("admin");
        return "loginAdmin_Staff";
    }

    public boolean checkLogin(Model model, HttpSession session) {
        Staff staff = (Staff) session.getAttribute("staff");
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin != null) {
            model.addAttribute("admin", admin);
            return true;
        } else if (staff != null) {
            model.addAttribute("staff", staff);
            return true;
        } else {
            return false;
        }
    }

    public String isLogin(Model model, HttpSession session) {
        boolean flag = checkLogin(model, session);
        if (!flag) {
            return getLoginPage(model, session);
        }
        return null;
    }

    public void isUserLogin(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }
    }

}
