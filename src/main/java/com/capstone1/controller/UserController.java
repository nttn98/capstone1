package com.capstone1.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

import com.capstone1.model.Cart;
import com.capstone1.model.CartItem;
import com.capstone1.model.Order;
import com.capstone1.model.Order.PaymentType;
import com.capstone1.model.OrderDetail;
import com.capstone1.model.Product;
import com.capstone1.model.User;
import com.capstone1.model.User.LoginType;
import com.capstone1.services.CartItemService;
import com.capstone1.services.CartService;
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
    @Autowired
    HomeController homeController;
    @Autowired
    ProductController productController;
    @Autowired
    private Encoding encoding;

    @GetMapping("/users")
    public String listUsers(Model model, HttpSession session, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        String target = homeController.isLogin(model, session);
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
    public String updateUser(Model model, @ModelAttribute User user, @RequestParam String mode,
            @RequestParam String email,
            HttpSession session, @RequestParam("id") Long userId, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
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
            return listUsers(model, session, page, size);
        } else {
            session.setAttribute("user", existUser);
            return homeController.getHome(model, session, page, size);
        }
    }

    @GetMapping("/users/change-status/{id}")
    public String changeStatus(@PathVariable Long id, Model model, @ModelAttribute User user, HttpSession session,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        User existUser = userService.getUserById(id);

        if (existUser.getStatus() == 0) {
            existUser.setStatus(1);
        } else {
            existUser.setStatus(0);
        }
        model.addAttribute("alert", "edit");
        userService.updateUser(existUser);
        return listUsers(model, session, page, size);
    }

    @GetMapping("/users/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/users";
    }

    @PostMapping("/users/save-user")
    public String saveUser(Model model, @ModelAttribute User user, @RequestParam String mode,
            HttpSession session, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        user.setPassword(encoding.toSHA1(user.getPassword()));
        user.setType(LoginType.LOCAL);
        user.setStatus(1);
        userService.saveUser(user);
        System.out.println("User added successfully");
        model.addAttribute("alert", "successRegister");
        if (mode.equals("user")) {
            return homeController.getHome(model, session, page, size);
        } else {
            return listUsers(model, session, page, size);
        }
    }

    /* Change password */
    @GetMapping("/users/to-change-pass/{id}")
    public String changePass(@PathVariable Long id, Model model, @ModelAttribute User user) {
        User existUser = userService.getUserById(id);
        System.out.println("--------------------" + existUser.getPassword());
        model.addAttribute("user", existUser);
        return "users/changePass_user";
    }

    @PostMapping("users/do-change-pass")
    public String changePasswod(@RequestParam Long id, Model model, @ModelAttribute User user,
            @RequestParam("oldPassword") String oldPass, @RequestParam("newPassword") String newPass,
            HttpSession session, @RequestParam String mode, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        User existUser = userService.getUserById(id);
        String oldUserPass = existUser.getPassword();

        String oldPassword = encoding.toSHA1(oldPass);

        if (oldUserPass.equals(oldPassword)) {
            existUser.setPassword(newPass);
            saveUser(model, existUser, mode, session, page, size);
            System.out.println("---------------------Success " + existUser.getPassword());
            model.addAttribute("alert", "changePass");

        } else {
            model.addAttribute("alert", "error");
            System.out.println("---------------------Fail");
        }

        if (mode.equals("user")) {
            session.removeAttribute("user");
            return homeController.getHome(model, session, page, size);
        } else {
            return "redirect:/users";
        }

    }

    /* check username is unique */
    @GetMapping("/checkUsernameAvailability")
    @ResponseBody // Ensure the returned boolean is serialized as a response body
    public ResponseEntity<Boolean> checkUsernameAvailability(@RequestParam("username") String username) {
        try {
            if (!userService.checkUsername(username) && !staffService.checkUsername(username)) {
                return ResponseEntity.ok(true); // Username is available
            } else {
                return ResponseEntity.ok(false); // Username is not available
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @GetMapping("/checkEmailAvailability")
    @ResponseBody // Ensure the returned boolean is serialized as a response body
    public ResponseEntity<Boolean> checkEmailAvailability(@RequestParam("email") String email,
            @RequestParam("originalEmail") String originalEmail) {

        try {
            if (!userService.checkEmail(email, originalEmail) && !staffService.checkEmail(email, originalEmail)) {
                return ResponseEntity.ok(true); // Email is available
            } else {
                return ResponseEntity.ok(false); // Email is not available
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    /* login user */
    @PostMapping("/login-user")
    public String getLoginUser(Model model, @RequestParam String username,
            @RequestParam String password, HttpSession session, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String typeLogin = (String) model.getAttribute("type");

        User user = null;
        if (typeLogin != null && typeLogin.equals("EMAIL")) {
            user = userService.findByGgID((String) model.getAttribute("ggId"));
        } else {
            String passEncoding = encoding.toSHA1(password);
            user = userService.findByUsernameAndPassword(username, passEncoding);
        }

        if (user != null && user.getStatus() == 1) {
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
            return homeController.getHome(model, session, page, size);
        }

        model.addAttribute("alert", "error");
        return homeController.getHome(model, session, page, size);
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
            @RequestParam(defaultValue = "1") int quantityInput, @RequestParam("mode") String mode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {

        Long userId = (Long) session.getAttribute("userId");
        Cart cart = (Cart) session.getAttribute("cart");
        Product temp = productService.getProductById(productId);

        if (userId == null) {
            addToCartWithoutUser(session, cart, temp, quantityInput);
        } else {
            User user = userService.getUserById(userId);
            addToCartWithUser(session, cart, temp, user, quantityInput);
        }

        if (mode.equals("inList")) {
            return homeController.paginated(model, session, page, size);
        } else if (mode.equals("inHome")) {
            return "redirect:/home-page";
        } else if (mode.equals("inProductDetail")) {
            return productController.getProductInfor(productId, model, session);
        } else {
            return "redirect:/home-page";

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

    @GetMapping("/users/total")
    @ResponseBody
    public int getTotalCartValue(@RequestParam("cartId") Long cartId) {
        Cart cart = cartService.findById(cartId);
        int total = cart.getTotal();
        return total;
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
            if (cartItem.getQuantity() == 0) {
                cartItemService.deleteByProductIdAndCartId(productId, cartId);
            }
            session.setAttribute("cart", cartService.findByUserId(user.getId()));
        }
        // if (user == null) {
        // Cart cart = (Cart) session.getAttribute("cart");
        // List<CartItem> iCartItem = cart.getListItem();
        // }

        return homeController.getHome(model, session, 0, 10);
    }

    @GetMapping("/users/delete-product-in-cart")
    public String deleteToCart(Model model, @RequestParam("productId") long productId, HttpSession session,
            @RequestParam("mode") String mode, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {

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
        if (mode.equals("inList")) {
            return homeController.paginated(model, session, page, size);
        } else if (mode.equals("inCheckout")) {
            return checkOut(model, session);
        } else if (mode.equals("inProductDetail")) {
            return productController.getProductInfor(productId, model, session);
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

        homeController.isUserLogin(model, session);
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
        double total = 0;

        if (cart != null && userLogin != null) {
            order = orderService.saveOrder(new Order(0, userLogin, now));
            for (int i = 0; i < cart.getListItem().size(); i++) {
                Product product = cart.getListItem().get(i).getProduct();
                Product productInDb = productService.getProductById(product.getId());

                double subtotal = product.getPrice();
                CartItem tempProduct = cartItemService.findByCartIdAndProductId(cart.getId(), product.getId());

                OrderDetail tempOrderDetail = orderDetailService
                        .saveOrderDetail(new OrderDetail(order, product, tempProduct.getQuantity(), subtotal));

                // change quantity after order
                productInDb.setQuantity(productInDb.getQuantity() - tempProduct.getQuantity());

                cartItemService.deleteByProductIdAndCartId(product.getId(), cart.getId());
                total += tempOrderDetail.getFinalPrice();

            }

            order.setReceiverName(name);
            order.setReceiverAddress(address);
            order.setReceiverNumberphone(numberphone);
            order.setTotal(total);
            order.setType(PaymentType.COD);
            cartService.deleteByUserId(userLogin.getId());
            cart.getListItem().clear();
        }
        session.removeAttribute("cart");
        orderService.saveOrder(order);
        return "redirect:/home-page";
    }

    // get order by user id
    @GetMapping("/users/order-history/{userId}")
    public String historyOrders(@PathVariable long userId, HttpSession session, Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        HashMap<Integer, String> map = new HashMap<>();
        map.put(0, "Prepared");
        map.put(1, "Shipping");
        map.put(2, "Delivered");
        map.put(3, "Canceled");

        homeController.isUserLogin(model, session);
        Page<Order> orders = orderService.findByUserId(userId, PageRequest.of(page, size, Sort.by("id").descending()));
        for (Order order : orders) {
            order.setShowStatus(map.get(order.getStatus()));
        }
        model.addAttribute("orders", orders);
        return "users/order_history";
    }

    /* Change Status */
    @GetMapping("/orders/user-change-status/{id}")
    public String userChangeOrderStatus(@PathVariable Long id, Model model, @ModelAttribute Order order,
            HttpSession session, @RequestParam("status") int newStatus, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Order existOrder = orderService.getOrderById(id);
        if (existOrder.getStatus() != newStatus) {
            existOrder.setStatus(newStatus);
        }
        User user = (User) session.getAttribute("user");

        orderService.changeStatusOrder(existOrder);
        return historyOrders(user.getId(), session, model, page, size);

    }

    @GetMapping("/users/order-detail/{orderId}")
    @ResponseBody
    public List<OrderDetail> orderDetail(@PathVariable long orderId, Model model) {
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
        model.addAttribute("orderDetails", orderDetails);
        return orderDetails;
    }

}
