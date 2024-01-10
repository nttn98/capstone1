package com.capstone1.controller;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import com.capstone1.model.Cart;
import com.capstone1.model.CartItem;
import com.capstone1.model.Order;
import com.capstone1.model.OrderDetail;
import com.capstone1.model.Product;
import com.capstone1.model.User;
import com.capstone1.services.CartItemService;
import com.capstone1.services.CartService;
import com.capstone1.services.OrderDetailService;
import com.capstone1.services.OrderService;
import com.capstone1.services.ProductService;
import com.capstone1.services.UserService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Resource
    UserService userService;
    @Resource
    OrderService orderService;
    @Resource
    OrderDetailService orderDetailService;
    @Resource
    CartService cartService;
    @Resource
    CartItemService cartItemService;
    @Resource
    ProductService productService;
    @Autowired
    HomeController homeController;
    @Autowired
    private Encoding encoding;

    @GetMapping("/users")
    public String listUsers(Model model, HttpSession session) {

        homeController.isLogin(model, session);
        List<User> listUsers = userService.getAllUsers();

        if (listUsers.size() == 0) {
            User user = new User();
            model.addAttribute("user", user);
            return "users/create_user";
        } else {
            model.addAttribute("users", listUsers);
            return "users/users";
        }

    }

    @GetMapping("/users/create-user")
    public String createUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "users/create_user";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "users/edit_user";
    }

    @PostMapping("/users/update-user")
    public String updateUser(Model model, @ModelAttribute("user") User user, @RequestParam("mode") String mode,
            HttpSession session, @RequestParam("id") Long userId) {
        User existUser = userService.getUserById(userId);

        existUser.setFullname(user.getFullname());
        existUser.setNumberphone(user.getNumberphone());
        existUser.setAddress(user.getAddress());
        existUser.setEmail(user.getEmail());
        existUser.setDob(user.getDob());

        userService.updateUser(existUser);
        System.out.println("User edited successfully");
        model.addAttribute("alert", "edit");

        if (mode.equals("user")) {
            session.setAttribute("user", existUser);
            return homeController.getHome(model, session);
        } else {
            return listUsers(model, session);
        }
    }

    @GetMapping("/users/change-status/{id}")
    public String changeStatus(@PathVariable Long id, Model model, @ModelAttribute("user") User user) {
        User existUser = userService.getUserById(id);

        if (existUser.getStatus() == 0) {
            existUser.setStatus(1);
        } else {
            existUser.setStatus(0);
        }

        userService.updateUser(existUser);
        return "redirect:/users";
    }

    @GetMapping("/users/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/users";
    }

    @PostMapping("/users/save-user")
    public String saveUser(Model model, @ModelAttribute("user") User user, @RequestParam("mode") String mode,
            HttpSession session) {

        user.setPassword(encoding.toSHA1(user.getPassword()));
        userService.saveUser(user);
        System.out.println("User added successfully");
        model.addAttribute("alert", "successRegister");
        if (mode.equals("user")) {
            return homeController.getHome(model, session);
        } else {
            return listUsers(model, session);
        }
    }

    /* Change password */
    @GetMapping("/users/to-change-pass/{id}")
    public String changePass(@PathVariable Long id, Model model, @ModelAttribute("user") User user) {
        User existUser = userService.getUserById(id);
        System.out.println("--------------------" + existUser.getPassword());
        model.addAttribute("user", existUser);
        return "users/changePass_user";
    }

    @PostMapping("users/do-change-pass")
    public String changePasswod(@RequestParam Long id, Model model, @ModelAttribute("user") User user,
            @RequestParam("oldPassword") String oldPass, @RequestParam("newPassword") String newPass,
            HttpSession session, @RequestParam("mode") String mode) {
        User existUser = userService.getUserById(id);
        String oldUserPass = existUser.getPassword();

        String oldPassword = encoding.toSHA1(oldPass);

        if (oldUserPass.equals(oldPassword)) {
            existUser.setPassword(newPass);
            saveUser(model, existUser, mode, session);
            System.out.println("---------------------Success " + existUser.getPassword());
            model.addAttribute("alert", "changePass");

        } else {
            model.addAttribute("alert", "error");
            System.out.println("---------------------Fail");
        }

        if (mode.equals("user")) {
            return homeController.getHome(model, session);
        } else {
            return "redirect:/users";
        }

    }

    /* login user */
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
            return homeController.getHome(model, session);
        }

        model.addAttribute("alert", "error");
        return homeController.getHome(model, session);
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

    /* order user */
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

    @GetMapping("/users/purchase-page")
    public String purchasePage(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        User userLogin = userService.getUserById(userId);
        session.setAttribute("user", userLogin);
        Cart cart = (Cart) session.getAttribute("cart");

        homeController.isUserLogin(model, session);
        model.addAttribute("cart", cart);
        return "admin/purcharsePage.html";
    }

    @GetMapping("/users/add-order")
    public String addOrder(Model model, HttpSession session, @RequestParam("name") String name,
            @RequestParam("address") String address, @RequestParam("numberphone") long numberphone) {
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
            order.setReceiverName(name);
            order.setReceiverAddress(address);
            order.setReceiverNumberphone(numberphone);
            order.setTotal(total);
            cartService.deleteByUserId(userLogin.getId());
            cart.getListItem().clear();

        }
        session.removeAttribute("cart");
        orderService.saveOrder(order);
        return "redirect:/home-page";
    }

    // get order by user id
    @GetMapping("/users/order-history/{userId}")
    public String historyOrders(@PathVariable long userId, HttpSession session, Model model) {
        homeController.isUserLogin(model, session);
        List<Order> orders = orderService.findByUserId(userId);
        model.addAttribute("orders", orders);
        return "users/order_history";
    }

    @GetMapping("/users/order-detail/{orderId}")
    @ResponseBody
    public List<OrderDetail> orderDetail(@PathVariable long orderId, Model model) {
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
        model.addAttribute("orderDetails", orderDetails);
        return orderDetails;

    }
}
