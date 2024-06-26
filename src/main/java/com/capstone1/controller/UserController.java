package com.capstone1.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.capstone1.model.Cart;
import com.capstone1.model.CartItem;
import com.capstone1.model.Notification;
import com.capstone1.model.Order;
import com.capstone1.model.Order.PaymentType;
import com.capstone1.model.OrderDetail;
import com.capstone1.model.Product;
import com.capstone1.model.User;
import com.capstone1.model.User.LoginType;
import com.capstone1.services.CartItemService;
import com.capstone1.services.CartService;
import com.capstone1.services.CommonService;
import com.capstone1.services.NotificationService;
import com.capstone1.services.OrderDetailService;
import com.capstone1.services.OrderService;
import com.capstone1.services.ProductService;
import com.capstone1.services.StaffService;
import com.capstone1.services.UserService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Resource
    CommonService commonService;
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
    @Resource
    StaffService staffService;
    @Resource
    NotificationService notificationService;
    @Autowired
    HomeController homeController;
    @Autowired
    ProductController productController;
    @Autowired
    private Encoding encoding;

    @GetMapping("/users")
    public String listUsers(Model model, HttpSession session) {

        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        List<User> listUsers = userService.getAll();

        if (listUsers.isEmpty()) {
            User user = new User();
            model.addAttribute("user", user);
            return "users/create_user";
        } else {
            model.addAttribute("users", listUsers);
            return "users/users";
        }

    }

    @GetMapping("/users/create-user")
    public String createUser(Model model, HttpSession session) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        User user = new User();
        model.addAttribute("user", user);
        return "users/create_user";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model, HttpSession session) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "users/edit_user";
    }

    @PostMapping("/users/update-user")
    public String updateUser(Model model, @ModelAttribute User user, @RequestParam String mode,
            @RequestParam String email, HttpSession session, @RequestParam("id") Long userId) {
                
        if (!mode.equals("user")) {
            String target = commonService.isLogin(model, session);
            if (target != null) {
                return target;
            }
        }

        User existUser = userService.getUserById(userId);
        model.addAttribute("alert", "edit");
        existUser.setFullname(user.getFullname());
        existUser.setNumberphone(user.getNumberphone());
        existUser.setAddress(user.getAddress());
        existUser.setEmail(user.getEmail());
        existUser.setDob(user.getDob());
        userService.updateUser(existUser);
        System.out.println("User edited successfully");
        if (mode.equals("adminEdit")) {
            return listUsers(model, session);
        } else {
            session.setAttribute("user", existUser);
            return homeController.getHome(model, session, 0, 10);
        }
    }

    @GetMapping("/users/change-status/{id}")
    public String changeStatus(@PathVariable Long id, Model model, @ModelAttribute User user, HttpSession session) {

        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }

        User existUser = userService.getUserById(id);

        if (existUser.getStatus() == 0) {
            existUser.setStatus(1);
        } else {
            existUser.setStatus(0);
        }
        model.addAttribute("alert", "edit");
        userService.updateUser(existUser);
        return listUsers(model, session);
    }

    @GetMapping("/users/delete-user/{id}")
    public String deleteUser(@PathVariable Long id, Model model, HttpSession session) {
        String target = commonService.isLogin(model, session);
        if (target != null) {
            return target;
        }
        userService.deleteUserById(id);
        return "redirect:/users";
    }

    @PostMapping("/users/save-user")
    public String saveUser(Model model, @ModelAttribute User user, @RequestParam String mode,
            HttpSession session) {

        user.setPassword(encoding.toSHA1(user.getPassword()));
        user.setType(LoginType.LOCAL);
        user.setStatus(1);
        userService.saveUser(user);
        System.out.println("User added successfully");
        model.addAttribute("alert", "successRegister");
        if (mode.equals("user")) {
            return homeController.getHome(model, session, 0, 10);
        } else {
            String target = commonService.isLogin(model, session);
            if (target != null) {
                return target;
            }
            return listUsers(model, session);
        }
    }

    @PostMapping("users/do-change-pass")
    public String changePasswod(@RequestParam Long id, Model model, @ModelAttribute User user,
            @RequestParam("oldPassword") String oldPass, @RequestParam("newPassword") String newPass,
            HttpSession session, @RequestParam String mode) {
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
            session.removeAttribute("user");
            return homeController.getHome(model, session, 0, 10);
        } else {
            String target = commonService.isLogin(model, session);
            if (target != null) {
                return target;
            }
            return listUsers(model, session);
        }
    }

    /* login user */
    @PostMapping("/login-user")
    public String getLoginUser(Model model, @RequestParam String username,
            @RequestParam String password, HttpSession session) {
        String typeLogin = (String) model.getAttribute("type");

        User user = null;
        if (typeLogin != null && typeLogin.equals("EMAIL")) {
            user = userService.findByGgID((String) model.getAttribute("ggId"));
        } else {
            String passEncoding = encoding.toSHA1(password);
            user = userService.findByUsernameAndPassword(username, passEncoding);
        }

        if (user != null && user.getStatus() == 1) {

            List<Notification> notifications = (List<Notification>) notificationService
                    .findByUserIdOrderByIdDesc(user.getId());
            user.setNotifications(notifications);

            session.setAttribute("userId", user.getId());
            session.setAttribute("user", user);

            model.addAttribute("alert", "success");

            Cart oldCart = (Cart) session.getAttribute("cart");
            Cart cart = cartService.findByUserId(user.getId());

            if (cart == null) {
                cart = cartService.saveCart(new Cart(user));
            }
            session.removeAttribute("cart");
            if (oldCart != null) {
                for (CartItem item : oldCart.getListItem()) {
                    Product product = item.getProduct();
                    CartItem cartItem = cartItemService.findByCartIdAndProductId(cart.getId(), product.getId());
                    if (cartItem == null) {
                        cartItem = new CartItem(cart, product, item.getQuantity());
                    } else {
                        cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                    }
                    cartItemService.save(cartItem);
                    cart.addCartItem(cartItem);
                }
            }

            session.setAttribute("cart", cart);

            return homeController.getHome(model, session, 0, 10);
        }

        model.addAttribute("alert", "error");
        return homeController.getHome(model, session, 0, 10);
    }

    @GetMapping("/users/logout")
    public String logOutUser(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            session.removeAttribute("userId");
            session.removeAttribute("user");
            session.removeAttribute("oldCart");
            session.removeAttribute("cart");
        }

        return "redirect:/home-page";
    }

    /* order user */
    @GetMapping("/users/add-to-cart/{productId}")
    public String addToCart(Model model, @PathVariable long productId, HttpSession session,
            @RequestParam(defaultValue = "1") int quantityInput, @RequestParam("mode") String mode) {

        Long userId = (Long) session.getAttribute("userId");
        Cart cart = (Cart) session.getAttribute("cart");
        Product temp = productService.getProductById(productId);

        if (userId == null) {
            addToCartWithoutUser(session, cart, temp, quantityInput);
        } else {
            User user = userService.getUserById(userId);
            addToCartWithUser(session, cart, temp, user, quantityInput);
        }
        model.addAttribute("alert", "addToCartS");
        if (mode.equals("inList")) {
            return homeController.getAllProductsForUser(model, session);
        } else if (mode.equals("inProductDetail")) {
            return productController.getProductInfor(productId, model, session);
        } else if (mode.equals("inSearchingPage")) {
            String keyword = (String) session.getAttribute("keywords");
            return productController.searchProduct(model, session, keyword);
        } else {
            return homeController.getHome(model, session, 0, 10);
        }
    }

    private void addToCartWithUser(HttpSession session, Cart cart, Product product, User user, int quantityInput) {
        if (cart == null) {
            cart = cartService.saveCart(new Cart(user));
        }
        CartItem cartItem = cartItemService.findByCartIdAndProductId(cart.getId(), product.getId());

        if (cartItem == null) {
            cartItem = cartItemService.save(new CartItem(cart, product, quantityInput));
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantityInput);
            cartItemService.save(cartItem);
        }

        cart.addProduct(product, cart, quantityInput);
        session.setAttribute("cart", cart);
    }

    private void addToCartWithoutUser(HttpSession session, Cart cart, Product product, int quantityInput) {
        if (cart == null) {
            cart = new Cart();
        }
        cart.addProduct(product, cart, quantityInput);
        session.setAttribute("cart", cart);

    }

    @GetMapping("/users/update-quantity-in-cart")
    public String updateQuantityInCart(@RequestParam("productId") long productId,
            @RequestParam("quantity") int quantity,
            @RequestParam("cartId") long cartId, Model model, HttpSession session,
            @RequestParam("mode") String mode) {
        quantity = 1;
        User user = (User) session.getAttribute("user");
        if (user != null) {
            CartItem cartItem = cartItemService.findByCartIdAndProductId(cartId,
                    productId);
            if (mode.equals("minus")) {
                cartItem.setQuantity(cartItem.getQuantity() - quantity);
            } else if (mode.equals("plus")) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            }
            cartItemService.updateQuantityInCart(cartItem);

            session.setAttribute("cart", cartService.findByUserId(user.getId()));
        } else {
            Cart cart = (Cart) session.getAttribute("cart");
            List<CartItem> iCartItem = cart.getListItem();

            CartItem cartItem = iCartItem.stream().filter(c -> c.product.getId() == productId).findFirst().get();

            if (mode.equals("minus")) {
                cartItem.setQuantity(cartItem.getQuantity() - quantity);
            } else if (mode.equals("plus")) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            }
            if (cartItem.getQuantity() == 0) {
                iCartItem.remove(cartItem);
            }

            session.setAttribute("cart", cart);
        }
        return homeController.getHome(model, session, 0, 10);
    }

    @GetMapping("/users/delete-product-in-cart")
    public String deleteToCart(Model model, @RequestParam("productId") long productId, HttpSession session,
            @RequestParam("mode") String mode, @RequestParam(defaultValue = "0") long currentProductId) {

        Cart cart = (Cart) session.getAttribute("cart");
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) { // without login
            cart.deleteByProductId(productId);
            if (cart.getTotal() == 0) {
                cart = null;
                session.removeAttribute("cart");
            }
        } else { // with login
            cartItemService.deleteByProductIdAndCartId(productId, cart.getId());
            cart.removeItem(productId);
            if (cart.getListItem().size() == 0) {
                cartService.deleteByUserId(userId);
                cart = null;
                session.removeAttribute("cart");
            }
        }
        // inOrderHistory, inSearchingPage
        if (mode.equals("inList")) {
            return homeController.getAllProductsForUser(model, session);
        } else if (mode.equals("inCheckout")) {
            return checkOut(model, session);
        } else if (mode.equals("inProductDetail")) {
            return productController.getProductInfor(currentProductId, model, session);
        } else if (mode.equals("inOrderHistory")) {
            if (userId != null)
                return historyOrders(userId, session, model);
        } else if (mode.equals("inSearchingPage")) {
            String keyword = (String) session.getAttribute("keywords");
            return productController.searchProduct(model, session, keyword);
        }

        session.setAttribute("cart", cart);
        return "redirect:/home-page";
    }

    @GetMapping("/users/checkout-page")
    public String checkOut(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        User userLogin = userService.getUserById(userId);
        session.setAttribute("user", userLogin);
        Cart cart = (Cart) session.getAttribute("cart");

        commonService.isUserLogin(model, session);
        model.addAttribute("cart", cart);
        return "admin/checkout.html";
    }

    /* user create order */
    @GetMapping("/users/add-order")
    public String addOrder(Model model, HttpSession session, HttpServletRequest request, @RequestParam String name,
            @RequestParam String address, @RequestParam long numberphone, @RequestParam String payment_method) {
        Long userId = (Long) session.getAttribute("userId");
        User userLogin = userService.getUserById(userId);
        Cart cart = (Cart) session.getAttribute("cart");
        LocalDateTime now = LocalDateTime.now();
        Order order = null;

        if (cart != null && userLogin != null) {
            order = orderService.saveOrder(new Order(0, userLogin, now));
            for (int i = 0; i < cart.getListItem().size(); i++) {
                Product product = cart.getListItem().get(i).getProduct();
                Product productInDb = productService.getProductById(product.getId());

                CartItem tempProduct = cartItemService.findByCartIdAndProductId(cart.getId(), product.getId());

                orderDetailService
                        .saveOrderDetail(new OrderDetail(order, product, tempProduct.getQuantity()));

                // change quantity after order
                productInDb.setQuantity(productInDb.getQuantity() - tempProduct.getQuantity());

                cartItemService.deleteByProductIdAndCartId(product.getId(), cart.getId());

            }

            order.setReceiverName(name);
            order.setReceiverAddress(address);
            order.setReceiverNumberphone(numberphone);
            order.setType(PaymentType.COD);
            cartService.deleteByUserId(userLogin.getId());
            cart.getListItem().clear();
        }
        session.removeAttribute("cart");
        orderService.saveOrder(order);
        model.addAttribute("alert", "paymentSuccess");
        // return "redirect:/home-page";
        return homeController.getHome(model, session, 0, 10);
    }

    /* user create order buy now */
    @GetMapping("/users/add-order-buy-now/{productId}")
    public String addOrderBuyNow(Model model, HttpSession session, HttpServletRequest request,
            @RequestParam String name, @PathVariable long productId,
            @RequestParam String address, @RequestParam long numberphone, @RequestParam String payment_method,
            @RequestParam(defaultValue = "1") int quantityInput) {
        Long userId = (Long) session.getAttribute("userId");
        User userLogin = userService.getUserById(userId);
        LocalDateTime now = LocalDateTime.now();
        Order order = null;

        order = orderService.saveOrder(new Order(0, userLogin, now));

        Product product = productService.getProductById(productId);

        if (product != null) {
            Product productInDb = productService.getProductById(product.getId());

            orderDetailService
                    .saveOrderDetail(new OrderDetail(order, product, quantityInput));

            // change quantity after order
            productInDb.setQuantity(productInDb.getQuantity() - quantityInput);

            order.setReceiverName(name);
            order.setReceiverAddress(address);
            order.setReceiverNumberphone(numberphone);
            order.setType(PaymentType.COD);
        }

        orderService.saveOrder(order);
        model.addAttribute("alert", "paymentSuccess");
        // return "redirect:/home-page";
        return homeController.getHome(model, session, 0, 10);
    }

    // user buy now
    @GetMapping("/users/buy-now/{productId}")
    public String buyNow(Model model, @PathVariable long productId, HttpSession session,
            @RequestParam(defaultValue = "1") int quantityInput) {

        commonService.isUserLogin(model, session);
        Product tempProduct = productService.getProductById(productId);
        model.addAttribute("product", tempProduct);
        model.addAttribute("quantityInput", quantityInput);
        return "admin/buyNow.html";
    }

    // get order by user id
    @GetMapping("/users/order-history/{userId}")
    public String historyOrders(@PathVariable long userId, HttpSession session, Model model) {
        HashMap<Integer, String> map = new HashMap<>();
        map.put(0, "Prepared");
        map.put(1, "Shipping");
        map.put(2, "Delivered");
        map.put(3, "Canceled");

        commonService.isUserLogin(model, session);
        List<Order> orders = orderService.findByUserIdOrderByIdDesc(userId);

        for (Order order : orders) {
            order.setShowStatus(map.get(order.getStatus()));
        }
        model.addAttribute("orders", orders);
        return "users/order_history";
    }

    /* Change Status */
    @GetMapping("/orders/user-change-status/{id}")
    public String userChangeOrderStatus(@PathVariable Long id, Model model, @ModelAttribute Order order,
            HttpSession session, @RequestParam("status") int newStatus) {

        Order existOrder = orderService.getOrderById(id);
        if (existOrder.getStatus() != newStatus) {
            existOrder.setStatus(newStatus);
        }
        User user = (User) session.getAttribute("user");

        orderService.changeStatusOrder(existOrder);
        return historyOrders(user.getId(), session, model);

    }

}
