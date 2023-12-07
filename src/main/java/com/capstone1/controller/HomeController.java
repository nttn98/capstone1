package com.capstone1.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.capstone1.model.*;
import com.capstone1.services.*;

import jakarta.annotation.Resource;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.*;

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

    @Autowired
    Encoding encoding;
    @Autowired
    JavaMailSender mailSender;

    @GetMapping({ "/home-page", "/" })
    public String getHome(Model model, HttpSession session) {

        List<Product> listProducts = productService.getAllProducts();
        List<Category> listCategories = categoryService.getAllCategories();
        List<Manufacturer> listManufacturers = manufacturerService.getAllManufacturers();

        model.addAttribute("products", listProducts);
        model.addAttribute("categories", listCategories);
        model.addAttribute("manufacturers", listManufacturers);

        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }

        return "homePage";
    }

    @GetMapping("/orders")
    public String listOrders(Model model) {
        List<Order> listOrders = orderService.getAllOrders();
        model.addAttribute("orders", listOrders);
        return "admin/orders";
    }

    @GetMapping("/orders/get-order-detail/{orderId}")
    @ResponseBody
    public List<OrderDetail> getOrderDetails(@PathVariable Long orderId, Model model) {
        List<OrderDetail> listOrderDetails = orderDetailService.findByOrderId(orderId);
        model.addAttribute("orderDetails", listOrderDetails);
        return listOrderDetails;
    }

    @GetMapping("/orders/change-status/{id}")
    public String changeStatus(@PathVariable Long id, Model model, @ModelAttribute("order") Order order) {
        Order existOrder = orderService.getOrderById(id);
        if (existOrder.getStatus() == 0) {
            existOrder.setStatus(1);
        } else {
            existOrder.setStatus(0);
        }
        model.addAttribute("alert", "success");
        orderService.changeStatusOrder(existOrder);
        return listOrders(model);
    }

    /* Order user */
    @GetMapping("/users/add-to-cart/{productId}")
    public String addToCart(Model model, @PathVariable long productId, HttpSession session,
            @RequestParam(name = "quantity", defaultValue = "1") int quantity) {
        Long userId = (Long) session.getAttribute("userId");
        Cart cart = (Cart) session.getAttribute("cart");
        Product temp = productService.getProductById(productId);

        if (userId == null) {
            addToCartWithoutUser(session, cart, temp);
        } else {
            User user = userService.getUserById(userId);
            addToCartWithUser(session, quantity, cart, temp, user);
        }

        return "redirect:/home-page";

    }

    private void addToCartWithUser(HttpSession session, int quantity, Cart cart, Product product, User user) {
        if (cart == null) {
            cart = cartService.saveCart(new Cart(user));
        }

        CartItem cartItem = cartItemService.findByProductId(product.getId());

        if (cartItem == null) {
            cartItem = cartItemService.save(new CartItem(cart, product, quantity));
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItemService.save(cartItem);
        }

        cart.addProduct(product, cart);
        session.setAttribute("cart", cart);
    }

    private void addToCartWithoutUser(HttpSession session, Cart cart, Product product) {
        if (cart == null) {
            cart = new Cart();
            cart.addProduct(product, null);
        } else {
            CartItem cartItem = cart.checkProductExist(product.getId());
            if (cartItem == null) {
                cartItem = new CartItem(cart, product, 1);
            }
            cart.addProduct(product, cart);
        }
        session.setAttribute("cart", cart);
    }

    @PostMapping("/users/delete-product-in-cart")
    public String deleteToCart(Model model, @RequestParam long productId, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) { // withLogin
            cart.deleteByProductId(productId);
        } else { // without login
            cartItemService.deleteByProductId(productId);
            cart.removeItem(productId);
            if (cart.getListItem().size() == 0) {
                cartService.deleteByUserId(userId);
                cart = null;
            }
        }
        session.setAttribute("cart", cart);
        return "redirect:/home-page";

    }

    @GetMapping("/users/add-order")
    public String addOrder(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        User userLogin = userService.getUserById(userId);
        Cart cart = (Cart) session.getAttribute("cart");
        LocalDateTime now = LocalDateTime.now();
        Order order = null;
        double total = 0;

        if (cart != null && userLogin != null) {
            order = orderService.saveOrder(new Order(0, userLogin, now));
            for (int i = 0; i < cart.getListItem().size(); i++) {
                Product product = cart.getListItem().get(i).getProduct();
                double subtotal = product.getPrice();
                OrderDetail tempOrderDetail = orderDetailService
                        .saveOrderDetail(new OrderDetail(order, product,
                                cartItemService.findByProductId(product.getId()).getQuantity(), subtotal));
                cartItemService.deleteByProductId(product.getId());
                total += tempOrderDetail.getFinalPrice();

            }
            order.setTotal(total);
            cartService.deleteByUserId(userLogin.getId());
            cart.getListItem().clear();

        }
        session.removeAttribute("cart");
        orderService.saveOrder(order);
        return "redirect:/home-page";

    }

    @GetMapping("/login-admin")
    public String getLogin(Model model) {
        return "loginAdmin_Staff";
    }

    @PostMapping("/login-user")
    public String getLoginUser(Model model, @RequestParam("username") String username,
            @RequestParam("password") String password, HttpSession session) {
        // List<User> listUsers = userService.getAllUsers();
        String passEncoding = encoding.toSHA1(password);
        User user = userService.findByUsernameAndPassword(username, passEncoding);

        if (user != null && user.getStatus() == 0) {
            session.setAttribute("userId", user.getId());
            session.setAttribute("user", user);
            model.addAttribute("alert", "success");

            Cart cart = cartService.findByUserId(user.getId());
            session.setAttribute("cart", cart);
            return getHome(model, session);
        }

        model.addAttribute("alert", "error");
        return getHome(model, session);
    }

    @GetMapping("/users/logout")
    public String logOutUser(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            session.removeAttribute("userId");
            session.removeAttribute("user");
            session.removeAttribute("cart");
        }

        return "redirect:/home-page";
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
            @RequestParam("password") String password, HttpSession httpSession) {

        System.out.println("----------------------" + password);
        TokenAdmin token = tokenAdminService.findByToken(tokenString);
        if (token == null) {
            model.addAttribute("flag", false);
            model.addAttribute("message", "Your token link is invalid!");
            return getLogin(model);
        }
        Staff staff = staffService.getStaffById(token.getStaffId());

        model.addAttribute("flag", true);
        model.addAttribute("message", "You have successfully changed your password.");

        staff.setPassword(encoding.toSHA1(password));

        tokenAdminService.deleteByStaffId(staff.getId());
        staffService.saveStaff(staff);

        httpSession.removeAttribute("staff");
        return getLogin(model);
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, HttpServletRequest request) {

        String tokenString = RandomStringUtils.randomAlphanumeric(60);

        Staff staff = staffService.findByEmail(email);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plusMinutes(30);

        tokenAdminService.save(new TokenAdmin(tokenString, staff.getId(), expirationTime));

        String siteURL = request.getRequestURL().toString();
        siteURL = siteURL.replace(request.getServletPath(), "");

        String resetPasswordLink = siteURL + "/reset-password?token=" + tokenString;
        sendEmail(email, resetPasswordLink);

        return "redirect:/loginAdmin";

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
            @RequestParam("password") String password, HttpSession httpSession) {

        System.out.println("---------user-------------" + password);
        System.out.println("---------user-------------" + encoding.toSHA1(password));

        TokenUser token = tokenUserService.findByToken(tokenString);
        if (token == null) {
            model.addAttribute("flag", false);
            model.addAttribute("message", "Your token link is invalid!");
            return getHome(model, httpSession);
        }
        User user = userService.getUserById(token.getUserId());

        model.addAttribute("flag", true);
        model.addAttribute("message", "You have successfully changed your password.");

        user.setPassword(encoding.toSHA1(password));

        tokenUserService.deleteByUserId(user.getId());
        userService.saveUser(user);

        httpSession.removeAttribute("user");

        return getHome(model, httpSession);
    }

    @PostMapping("/users/forgot-password")
    public String forgotPasswordForUser(@RequestParam("email") String email, HttpServletRequest request, Model model,
            HttpSession session) {

        String tokenString = RandomStringUtils.randomAlphanumeric(60);
        List<User> listUsers = userService.getAllUsers();
        // User existuser = userService.getUserByEmail(email);
        User existuser = userService.findByEmail(email);
        for (User user : listUsers) {
            if (!user.getEmail().equals(email)) {
                model.addAttribute("alert", "sendMailfail");
                return getHome(model, session);
            }
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plusMinutes(30);

        tokenUserService.save(new TokenUser(tokenString, existuser.getId(), expirationTime));

        String siteURL = request.getRequestURL().toString();
        siteURL = siteURL.replace(request.getServletPath(), "");

        String resetPasswordLink = siteURL + "/users/reset-password?token=" + tokenString;
        sendEmail(email, resetPasswordLink);
        model.addAttribute("user", existuser);
        model.addAttribute("alert", "sendMailsuccess");

        return getHome(model, session); // user

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