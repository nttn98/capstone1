package com.capstone1.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.capstone1.model.Admin;
import com.capstone1.model.BlogLogin;
import com.capstone1.model.BlogLogin.LoginStatus;
import com.capstone1.model.Category;
import com.capstone1.model.Contact;
import com.capstone1.model.Notification;
import com.capstone1.model.Order;
import com.capstone1.model.Product;
import com.capstone1.model.Staff;
import com.capstone1.model.TokenAdmin;
import com.capstone1.model.TokenUser;
import com.capstone1.model.User;
import com.capstone1.services.AdminService;
import com.capstone1.services.BlogLoginService;
import com.capstone1.services.CartItemService;
import com.capstone1.services.CartService;
import com.capstone1.services.CategoryService;
import com.capstone1.services.CommonService;
import com.capstone1.services.ContactServices;
import com.capstone1.services.ManufacturerService;
import com.capstone1.services.NotificationService;
import com.capstone1.services.OrderDetailService;
import com.capstone1.services.OrderService;
import com.capstone1.services.ProductService;
import com.capstone1.services.StaffService;
import com.capstone1.services.TokenAdminService;
import com.capstone1.services.TokenUserService;
import com.capstone1.services.UserService;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Resource
    TokenUserService tokenUserService;
    @Resource
    TokenAdminService tokenAdminService;
    @Resource
    StaffService staffService;
    @Resource
    UserService userService;
    @Resource
    ProductService productService;
    @Resource
    CategoryService categoryService;
    @Resource
    ManufacturerService manufacturerService;
    @Resource
    OrderService orderService;
    @Resource
    OrderDetailService orderDetailService;
    @Resource
    CartService cartService;
    @Resource
    CartItemService cartItemService;
    @Resource
    AdminService adminService;
    @Resource
    ContactServices contactServices;
    @Resource
    BlogLoginService blogLoginService;
    @Resource
    NotificationService notificationService;
    @Resource
    CommonService commonService;

    @Autowired
    Encoding encoding;
    @Autowired
    JavaMailSender mailSender;

    @Autowired
    ProductController productController;

    @GetMapping({ "/home-page", "/" })
    public String getHome(Model model, HttpSession session, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        int limit = 3;
        Page<Product> newests = productService.findByIsNewestAndStatus(1, 1, PageRequest.of(page, 1));
        List<Product> products = productService.getNewestProducts();
        products = products.subList(1, Math.min(size, products.size()));

        Page<Product> productsByNVIDIA = productService.findByManufacturerNameAndQuantityGreaterThanAndStatus(
                "nvidia", 0,
                1, PageRequest.of(page, limit));

        Page<Product> productsByAMD = productService.findByManufacturerNameAndQuantityGreaterThanAndStatus("amd",
                0,
                1, PageRequest.of(page, limit));

        model.addAttribute("newests", newests);
        model.addAttribute("products", products);
        model.addAttribute("productsByAMD", productsByAMD);
        model.addAttribute("productsByNVIDIA", productsByNVIDIA);

        commonService.isUserLogin(model, session);

        return "homePage";
    }

    @GetMapping("/list-products")
    public String getAllProductsForUser(Model model, HttpSession session) {
        List<Product> listProducts = null;

        listProducts = productService.findByStatus(1);

        List<Category> listCategories = categoryService.getAll();
        model.addAttribute("products", listProducts);
        model.addAttribute("categories", listCategories);
        commonService.isUserLogin(model, session);

        return "listProducts";
    }

    @GetMapping("/list-products/{categoryName}")
    public String getProductsByCategoryForUser(Model model, HttpSession session, @PathVariable String categoryName) {
        List<Product> listProducts = null;

        listProducts = productService.findByCategoryNameAndStatus(categoryName, 1);

        List<Category> listCategories = categoryService.getAll();
        model.addAttribute("products", listProducts);
        model.addAttribute("categories", listCategories);

        long idCategory = categoryService.findIdByName(categoryName);
        model.addAttribute("idCategory", idCategory);

        commonService.isUserLogin(model, session);

        return "listProducts";
    }

    @GetMapping("/dashBoard")
    public String getDashBoardPage(Model model, HttpSession session) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        List<BlogLogin> listBlogs = blogLoginService.getAllBlogLogins();
        model.addAttribute("blogs", listBlogs);
        return "admin/dashBoard";
    }

    @GetMapping("/login-admin")
    public String getLoginPage(Model model, HttpSession session) {
        session.removeAttribute("staff");
        session.removeAttribute("admin");

        return "loginAdmin_Staff";
    }

    private void saveBlogLogin(long id, boolean status) {
        LocalDateTime now = LocalDateTime.now();
        Staff staff = staffService.getStaffById(id);
        BlogLogin blogLogin = new BlogLogin();
        if (status == true) {
            blogLogin = new BlogLogin(staff, now, LoginStatus.LOGIN);
        } else {
            blogLogin = new BlogLogin(staff, now, LoginStatus.LOGOUT);
        }
        blogLoginService.saveBlogLogin(blogLogin);
    }

    @PostMapping("/login-admin")
    public String getLogin(Model model, HttpSession session, @RequestParam String username,
            @RequestParam String password) {

        String passEncoding = encoding.toSHA1(password);
        Staff checkStaff = staffService.findByUsernameAndPassword(username, passEncoding);
        Admin checkAdmin = adminService.findByUsernameAndPassword(username, password);

        if (checkAdmin != null) {
            session.setAttribute("admin", checkAdmin);
            model.addAttribute("alert", "success");
            return getDashBoardPage(model, session);
        } else if (checkStaff != null && checkStaff.getStatus() == 1) {
            // model.addAttribute("mode", mode);
            session.setAttribute("staff", checkStaff);
            model.addAttribute("alert", "success");
            saveBlogLogin(checkStaff.getId(), true);
            return productController.listProducts(model, session);
        } else {
            model.addAttribute("alert", "error");
            return getLoginPage(model, session);
        }

    }

    @GetMapping("/logout")
    public String getLogout(Model model, HttpSession session) {
        Staff s = (Staff) session.getAttribute("staff");
        if (s != null) {
            saveBlogLogin(s.getId(), false);
        }
        session.removeAttribute("admin");
        session.removeAttribute("staff");

        return getLoginPage(model, session);
    }

    // get all orders in admin mode
    @GetMapping("/orders")
    public String listOrders(Model model, HttpSession session) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        HashMap<Integer, String> map = new HashMap<>();
        map.put(0, "Prepared");
        map.put(1, "Shipping");
        map.put(2, "Delivered");
        map.put(3, "Canceled");

        commonService.isLogin(model, session);

        List<Order> orders = orderService.getAll();
        for (Order order : orders) {
            order.setShowStatus(map.get(order.getStatus()));
        }

        if (!orders.isEmpty()) {
            model.addAttribute("orders", orders);
        }

        // model.addAttribute("mode", "staff");

        return "admin/orders";
    }

    @GetMapping("/orders/delete/{orderId}")
    public String deleteOrder(@PathVariable Long orderId, Model model, HttpSession session) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        orderService.deleteOrderById(orderId);
        orderDetailService.deleteByOrderId(orderId);
        model.addAttribute("alert", "success");

        return listOrders(model, session);
    }

    /* Change order status */
    @GetMapping("/orders/change-status/{id}")
    public String changeOrderStatus(@PathVariable Long id, Model model, @ModelAttribute Order order,
            HttpSession session, @RequestParam("status") int newStatus) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        Order existOrder = orderService.getOrderById(id);

        HashMap<Integer, String> map = new HashMap<>();
        map.put(0, "Prepared");
        map.put(1, "Shipping");
        map.put(2, "Delivered");
        map.put(3, "Canceled");

        if (existOrder.getStatus() != newStatus) {
            String message = "The status of order " + existOrder.getId() + " has been changed from "
                    + map.get(existOrder.getStatus()) + " to " + map.get(newStatus);
            LocalDateTime now = LocalDateTime.now();
            existOrder.setStatus(newStatus);

            notificationService.save(new Notification(existOrder.getUser(), message, false, now));
        }

        model.addAttribute("alert", "edit");
        orderService.changeStatusOrder(existOrder);
        return listOrders(model, session);
    }

    /* contact */
    /* get all contact */
    @GetMapping("/contacts")
    public String listContacts(Model model, HttpSession session) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        List<Contact> listContacts = contactServices.getAll();
        model.addAttribute("contacts", listContacts);
        return "admin/contacts";
    }

    /* send contact of customer */
    @PostMapping("/send-contact")
    public String sendContact(Model model, @RequestParam("name") String name, @RequestParam("email") String email,
            @RequestParam("numberphone") Long numberphone, @RequestParam("message") String message,
            HttpSession session) {
        Contact contact = new Contact(name, email, numberphone, message);
        contactServices.saveContact(contact);
        autoReply(email, name);
        model.addAttribute("alert", "sendContact");
        return getHome(model, session, 0, 10);
    }

    /* Forgot password admin and staff */

    @GetMapping("/reset-password")
    public String toResetPasswordPage(@RequestParam("token") String tokenString, Model model) {
        TokenAdmin token = tokenAdminService.findByToken(tokenString);
        if (token == null)
            model.addAttribute("tokenExprired", true);

        model.addAttribute("token", tokenString);
        return "recover_password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(Model model, @RequestParam("token") String tokenString,
            @RequestParam String password, HttpSession session) {

        System.out.println("----------------------" + password);
        TokenAdmin token = tokenAdminService.findByToken(tokenString);
        if (token == null) {
            model.addAttribute("flag", false);
            model.addAttribute("message", "Your token link is invalid!");
            return getLoginPage(model, session);
        }
        Staff staff = staffService.getStaffById(token.getStaff().getId());

        model.addAttribute("flag", true);
        model.addAttribute("message", "You have successfully changed your password.");

        staff.setPassword(encoding.toSHA1(password));

        tokenAdminService.deleteByStaffId(staff.getId());
        staffService.saveStaff(staff);

        session.removeAttribute("staff");
        model.addAttribute("alert", "recoverPass");

        return getLoginPage(model, session);
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email, HttpServletRequest request, Model model,
            HttpSession session) {

        String tokenString = RandomStringUtils.randomAlphanumeric(60);

        Staff staff = staffService.findByEmail(email);
        if (staff == null) {
            model.addAttribute("alert", "sendMailfail");
            return getLoginPage(model, session);
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plusMinutes(30);

        tokenAdminService.save(new TokenAdmin(tokenString, staff, expirationTime));

        String siteURL = request.getRequestURL().toString();
        siteURL = siteURL.replace(request.getServletPath(), "");

        String resetPasswordLink = siteURL + "/reset-password?token=" + tokenString;
        sendEmail(email, resetPasswordLink);
        model.addAttribute("alert", "sendMailsuccess");

        return getLoginPage(model, session);

    }

    /* Forgot password user */

    @GetMapping("/users/reset-password")
    public String toResetPasswordPageForUser(@RequestParam("token") String tokenString, Model model) {
        TokenUser token = tokenUserService.findByToken(tokenString);
        if (token == null)
            model.addAttribute("tokenExprired", true);

        model.addAttribute("token", tokenString);
        return "/users/recover_password_user"; // user
    }

    @PostMapping("/users/reset-password")
    public String resetPasswordForUser(Model model, @RequestParam("token") String tokenString,
            @RequestParam String password, HttpSession session) {

        System.out.println("---------user-------------" + password);
        System.out.println("---------user-------------" + encoding.toSHA1(password));

        TokenUser token = tokenUserService.findByToken(tokenString);
        if (token == null) {
            model.addAttribute("flag", false);
            model.addAttribute("message", "Your token link is invalid!");
            return getHome(model, session, 0, 10);
        }
        User user = userService.getUserById(token.getUser().getId());

        model.addAttribute("flag", true);
        model.addAttribute("message", "You have successfully changed your password.");

        user.setPassword(encoding.toSHA1(password));

        tokenUserService.deleteByUserId(user.getId());
        userService.saveUser(user);

        session.removeAttribute("user");

        return getHome(model, session, 0, 10);
    }

    @PostMapping("/users/forgot-password")
    public String forgotPasswordForUser(@RequestParam String email, HttpServletRequest request, Model model,
            HttpSession session) {

        String tokenString = RandomStringUtils.randomAlphanumeric(60);

        User existuser = userService.findByEmail(email);

        if (existuser == null) {
            model.addAttribute("alert", "sendMailfail");
            return getHome(model, session, 0, 10);
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plusMinutes(30);

        tokenUserService.save(new TokenUser(tokenString, existuser, expirationTime));

        String siteURL = request.getRequestURL().toString();
        siteURL = siteURL.replace(request.getServletPath(), "");

        String resetPasswordLink = siteURL + "/users/reset-password?token=" + tokenString;
        sendEmail(email, resetPasswordLink);
        model.addAttribute("user", existuser);
        model.addAttribute("alert", "sendMailsuccess");

        return getHome(model, session, 0, 10); // user

    }

    /* send email */
    public void sendEmail(String recipientEmail, String link) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(recipientEmail);
            helper.setFrom("contact@shopme.com", "NAP Support");

            String subject = "Here's the link to reset your password";
            String content = "<p>Hello,</p>"
                    + "<p>You have requested to reset your password.</p>"
                    + "<p>Click the link below to reset your password:</p>"
                    + "<p><a href=\"" + link + "\">" + link + "</a></p>"
                    + "<br/>"
                    + "<p>Ignore this email if you do remember your password, "
                    + "or you have not made the request.</p>";

            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /* auto reply email */
    public void autoReply(String recipientEmail, String name) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(recipientEmail);
            helper.setFrom("contact@shopme.com", "NAP Support");

            String subject = "Hello " + name;
            String content = "<p>Hello my valued customer,this is automated reply </p>"
                    + "<p>Thank you for trusting our service. We will reply to you as soon as possible.</p>";

            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}