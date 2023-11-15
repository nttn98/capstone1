package com.capstone1.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.capstone1.model.Staff;
import com.capstone1.model.Token;
import com.capstone1.model.User;
import com.capstone1.services.StaffService;
import com.capstone1.services.TokenService;
import com.capstone1.services.UserService;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.*;

@Controller
public class HomeController {

    @Resource
    TokenService tokenService;
    @Autowired
    JavaMailSender mailSender;

    private StaffService staffService;
    private UserService userService;
    private Encoding encoding;

    public HomeController(TokenService tokenService, JavaMailSender mailSender, StaffService staffService,
            UserService userService, Encoding encoding) {
        this.tokenService = tokenService;
        this.mailSender = mailSender;
        this.staffService = staffService;
        this.userService = userService;
        this.encoding = encoding;
    }

    @GetMapping({ "/homePage", "/" })
    public String getHome(Model model) {
        return "homePage";
    }

    @GetMapping("/loginAdmin")
    public String getLogin(Model model) {
        return "loginAdmin_Staff";
    }

    @GetMapping("/loginUser")
    public String getLoginUser(Model model) {
        return "users/login_user";
    }

    /* Forgot password admin and staff */

    @GetMapping("/reset-password")
    public String toResetPasswordPage(@RequestParam("token") String tokenString, Model model) {
        Token token = tokenService.findByToken(tokenString);
        if (token == null)
            model.addAttribute("tokenExprired", true);

        model.addAttribute("token", tokenString);
        return "recover_password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(Model model, @RequestParam("token") String tokenString,
            @RequestParam("password") String password, HttpSession httpSession) {

        System.out.println("----------------------" + password);
        Token token = tokenService.findByToken(tokenString);
        if (token == null) {
            model.addAttribute("flag", false);
            model.addAttribute("message", "Your token link is invalid!");
            return getLogin(model);
        }
        Staff staff = staffService.getStaffById(token.getUserId());

        model.addAttribute("flag", true);
        model.addAttribute("message", "You have successfully changed your password.");

        staff.setStaffPassword(encoding.toSHA1(password));

        tokenService.deleteByUserId(staff.getStaffId());
        staffService.saveStaff(staff);

        httpSession.removeAttribute("staff");
        return getLogin(model);
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, HttpServletRequest request) {

        String tokenString = RandomStringUtils.randomAlphanumeric(60);

        Staff staff = staffService.getStaffByEmail(email);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plusMinutes(30);

        tokenService.save(new Token(tokenString, staff.getStaffId(), expirationTime));

        String siteURL = request.getRequestURL().toString();
        siteURL = siteURL.replace(request.getServletPath(), "");

        String resetPasswordLink = siteURL + "/reset-password?token=" + tokenString;
        sendEmail(email, resetPasswordLink);

        return "redirect:/loginAdmin";

    }

    /* Forgot password user */

    @GetMapping("/users/reset-password")
    public String toResetPasswordPageForUser(@RequestParam("token") String tokenString, Model model) {
        Token token = tokenService.findByToken(tokenString);
        if (token == null)
            model.addAttribute("tokenExprired", true);

        model.addAttribute("token", tokenString);
        return "recover_password_user"; // user
    }

    @PostMapping("/users/reset-password")
    public String resetPasswordForUser(Model model, @RequestParam("token") String tokenString,
            @RequestParam("password") String password, HttpSession httpSession) {

        System.out.println("---------user-------------" + password);
        System.out.println("---------user-------------" + encoding.toSHA1(password));

        Token token = tokenService.findByToken(tokenString);
        if (token == null) {
            model.addAttribute("flag", false);
            model.addAttribute("message", "Your token link is invalid!");
            return getLogin(model);
        }
        User user = userService.getUserById(token.getUserId());

        model.addAttribute("flag", true);
        model.addAttribute("message", "You have successfully changed your password.");

        user.setUserPassword(encoding.toSHA1(password));

        tokenService.deleteByUserId(user.getUserId());
        userService.saveUser(user);

        httpSession.removeAttribute("user");
        return getLogin(model);
    }

    @PostMapping("/users/forgot-password")
    public String forgotPasswordForUser(@RequestParam("email") String email, HttpServletRequest request) {

        String tokenString = RandomStringUtils.randomAlphanumeric(60);

        User user = userService.getUserByEmail(email);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plusMinutes(30);

        tokenService.save(new Token(tokenString, user.getUserId(), expirationTime));

        String siteURL = request.getRequestURL().toString();
        siteURL = siteURL.replace(request.getServletPath(), "");

        String resetPasswordLink = siteURL + "/users/reset-password?token=" + tokenString;
        sendEmail(email, resetPasswordLink);

        return "redirect:/login_user"; // user

    }

    /* send email */
    public void sendEmail(String recipientEmail, String link) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(recipientEmail);
            helper.setFrom("contact@shopme.com", "NAD Support");

            String subject = "Here's the link to reset your password";
            String content = "<p>Hello,</p>"
                    + "<p>You have requested to reset your password.</p>"
                    + "<p>Click the link below to reset your password:</p>"
                    + "<p><a href=\"" + link + "\">" + link + "</a></p>"
                    + "<br>"
                    + "<p>Ignore this email if you do remember your password, "
                    + "or you have not made the request.</p>";

            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}