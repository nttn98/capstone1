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
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import jakarta.annotation.Resource;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.*;
import jakarta.websocket.server.PathParam;

@Controller
public class HomeController {

    @Resource
    TokenUserService tokenUserService;
    @Resource
    TokenAdminService tokenAdminService;

    @Autowired
    JavaMailSender mailSender;

    private StaffService staffService;
    private UserService userService;
    private Encoding encoding;
    private ProductService productService;
    private CategoryService categoryService;
    private ManufacturerService manufacturerService;

    private OrderService orderService;
    private OrderDetailService orderDetailService;
    private CartService cartService;
    private CartItemService cartItemService;

    public HomeController(TokenUserService tokenUserService, TokenAdminService tokenAdminService,
            JavaMailSender mailSender, StaffService staffService, UserService userService, Encoding encoding,
            ProductService productService, CategoryService categoryService, ManufacturerService manufacturerService,
            OrderService orderService, OrderDetailService orderDetailService, CartService cartService,
            CartItemService cartItemService) {
        this.tokenUserService = tokenUserService;
        this.tokenAdminService = tokenAdminService;
        this.mailSender = mailSender;
        this.staffService = staffService;
        this.userService = userService;
        this.encoding = encoding;
        this.productService = productService;
        this.categoryService = categoryService;
        this.manufacturerService = manufacturerService;

        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.cartService = cartService;
        this.cartItemService = cartItemService;
    }

    @GetMapping({ "/homePage", "/" })
    public String getHome(Model model) {

        List<Product> listProducts = productService.getAllProducts();
        List<Category> listCategories = categoryService.getAllCategories();
        List<Manufacturer> listManufacturers = manufacturerService.getAllManufacturers();

        model.addAttribute("products", listProducts);
        model.addAttribute("categories", listCategories);
        model.addAttribute("manufacturers", listManufacturers);

        return "homePage";
    }

    @GetMapping("/orders")
    public String listOrders(Model model) {
        List<Order> listOrders = orderService.getAllOrders();
        List<OrderDetail> listOrderDetails = orderDetailService.getAllOrderDeitals();
        model.addAttribute("orders", listOrders);
        model.addAttribute("orderDetails", listOrderDetails);
        return "admin/orders";
    }

    @GetMapping("/orders/changeStatus/{id}")
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
    @GetMapping("/users/addToCart/{productId}")
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

        return "redirect:/homePage";

    }

    private void addToCartWithUser(HttpSession session, int quantity, Cart cart, Product product, User user) {
        if (cart == null) {
            cart = cartService.saveCart(new Cart(user));
        }

        CartItem cartItem = cartItemService.findByProductId(product.getProductId());

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
            CartItem cartItem = cart.checkProductExist(product.getProductId());
            if (cartItem == null) {
                cartItem = new CartItem(cart, product, 1);
            }
            cart.addProduct(product, cart);
        }
        session.setAttribute("cart", cart);
    }

    @GetMapping("/users/deleteProductInCart/{producId}")
    public String deleteToCart(Model model, @PathVariable long producId, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) { // withLogin
            cart.deleteByProductId(producId);
        } else { // without login
            cartItemService.deleteByProductId(producId);
            cart.removeItem(producId);
            if (cart.getListItem().size() == 0) {
                cartService.deleteByUserId(userId);
            }
        }
        session.setAttribute("cart", cart);
        return "redirect:/homePage";

    }

    @GetMapping("/users/add-order")
    public String addOrder(Model model, HttpSession session,
            @RequestParam(name = "quantity", defaultValue = "1") int quantity) {
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
                double subtotal = product.getProductPrice() * quantity;
                orderDetailService.saveOrderDetail(new OrderDetail(order, product, quantity, subtotal));
                cartItemService.deleteByProductId(product.getProductId());
                total += subtotal;
            }
            // cartService.deleteByUserId(userLogin.getUserId());
            order.setTotal(total);

            cartService.deleteByUserId(userLogin.getUserId());
            cart.getListItem().clear();

        }
        session.removeAttribute("cart");
        orderService.saveOrder(order);
        return "redirect:/homePage";

    }

    @GetMapping("/loginAdmin")
    public String getLogin(Model model) {
        return "loginAdmin_Staff";
    }

    @PostMapping("/login-user")
    public String getLoginUser(Model model, @RequestParam("username") String username,
            @RequestParam("password") String password, HttpSession session) {
        List<User> listUsers = userService.getAllUsers();
        String passEncoding = encoding.toSHA1(password);

        for (User user : listUsers) {
            if (user.getUserUsername().equals(username) && user.getUserPassword().equals(passEncoding)) {
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("user", user);
                model.addAttribute("alert", "success");

                Cart cart = cartService.findByUserId(user.getUserId());
                session.setAttribute("cart", cart);
            } else {
                model.addAttribute("alert", "error");
            }
        }

        return getHome(model);
    }

    @GetMapping("/users/logout")
    public String logOutUser(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            session.removeAttribute("user");
        }

        return "redirect:/homePage";
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

        staff.setStaffPassword(encoding.toSHA1(password));

        tokenAdminService.deleteByStaffId(staff.getStaffId());
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

        tokenAdminService.save(new TokenAdmin(tokenString, staff.getStaffId(), expirationTime));

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
            return getHome(model);
        }
        User user = userService.getUserById(token.getUserId());

        model.addAttribute("flag", true);
        model.addAttribute("message", "You have successfully changed your password.");

        user.setUserPassword(encoding.toSHA1(password));

        tokenUserService.deleteByUserId(user.getUserId());
        userService.saveUser(user);

        httpSession.removeAttribute("user");

        return getHome(model);
    }

    @PostMapping("/users/forgot-password")
    public String forgotPasswordForUser(@RequestParam("email") String email, HttpServletRequest request, Model model) {

        String tokenString = RandomStringUtils.randomAlphanumeric(60);
        List<User> listUsers = userService.getAllUsers();
        // User existuser = userService.getUserByEmail(email);
        User existuser = userService.findByEmail(email);
        for (User user : listUsers) {
            if (!user.getUserEmail().equals(email)) {
                model.addAttribute("alert", "sendMailfail");
                return getHome(model);
            }
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plusMinutes(30);

        tokenUserService.save(new TokenUser(tokenString, existuser.getUserId(), expirationTime));

        String siteURL = request.getRequestURL().toString();
        siteURL = siteURL.replace(request.getServletPath(), "");

        String resetPasswordLink = siteURL + "/users/reset-password?token=" + tokenString;
        sendEmail(email, resetPasswordLink);
        model.addAttribute("user", existuser);
        model.addAttribute("alert", "sendMailsuccess");

        return getHome(model); // user

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