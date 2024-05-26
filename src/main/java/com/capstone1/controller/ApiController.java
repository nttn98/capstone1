package com.capstone1.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.capstone1.model.Cart;
import com.capstone1.model.Category;
import com.capstone1.model.Manufacturer;
import com.capstone1.model.OrderDetail;
import com.capstone1.model.Product;
import com.capstone1.services.CartService;
import com.capstone1.services.CategoryService;
import com.capstone1.services.ManufacturerService;
import com.capstone1.services.OrderDetailService;
import com.capstone1.services.ProductService;
import com.capstone1.services.StaffService;
import com.capstone1.services.UserService;

import jakarta.annotation.Resource;

@RestController
public class ApiController {
    @Resource
    UserService userService;

    @Resource
    StaffService staffService;

    @Resource
    CartService cartService;

    @Resource
    OrderDetailService orderDetailService;

    @Resource
    CategoryService categoryService;

    @Resource
    ProductService productService;

    @Resource
    ManufacturerService manufacturerService;

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

    @GetMapping("/users/total")
    @ResponseBody
    public int getTotalCartValue(@RequestParam("cartId") Long cartId) {
        Cart cart = cartService.findById(cartId);
        int total = cart.getTotal();
        return total;
    }

    @GetMapping("/users/order-detail/{orderId}")
    @ResponseBody
    public List<OrderDetail> orderDetail(@PathVariable long orderId, Model model) {
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
        model.addAttribute("orderDetails", orderDetails);
        return orderDetails;
    }

    /* check name is unique */
    @GetMapping("/checkCategoryNameAvailability")
    @ResponseBody // Ensure the returned boolean is serialized as a response body
    public ResponseEntity<Boolean> checkCategoryNameAvailability(@RequestParam("name") String name) {
        try {
            Category categoryExist = categoryService.findByName(name);
            if (categoryExist == null) {
                return ResponseEntity.ok(true); // name is available
            } else {
                return ResponseEntity.ok(false); // name is not available
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @GetMapping("/orders/get-order-detail/{orderId}")
    @ResponseBody
    public List<OrderDetail> getOrderDetails(@PathVariable Long orderId, Model model) {
        List<OrderDetail> listOrderDetails = orderDetailService.findByOrderId(orderId);
        model.addAttribute("orderDetails", listOrderDetails);
        return listOrderDetails;
    }

    @GetMapping("/checkManuFactureNameAvailability")
    @ResponseBody // Ensure the returned boolean is serialized as a response body
    public ResponseEntity<Boolean> checkManuFactureNameAvailability(@RequestParam("name") String name) {
        try {
            Manufacturer manufacturerExist = manufacturerService.findByName(name);
            if (manufacturerExist == null) {
                return ResponseEntity.ok(true); // name is available
            } else {
                return ResponseEntity.ok(false); // name is not available
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @GetMapping("/checkProductNameAvailability")
    @ResponseBody // Ensure the returned boolean is serialized as a response body
    public ResponseEntity<Boolean> checkProductNameAvailability(@RequestParam("name") String name) {
        try {
            Product productExist = productService.findByName(name);
            if (productExist == null) {
                return ResponseEntity.ok(true); // name is available
            } else {
                return ResponseEntity.ok(false); // name is not available
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @GetMapping("/checkQuantity")
    @ResponseBody
    public ResponseEntity<Boolean> checkQuantity(@RequestParam("quantityInCart") int quantityIncart, long id) {
        try {
            Product productExist = productService.getProductById(id);
            if (quantityIncart <= productExist.getQuantity()) {
                return ResponseEntity.ok(true); // name is available
            } else {
                return ResponseEntity.ok(false); // name is not available
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @GetMapping("/checkIdcardAvailability")
    @ResponseBody // Ensure the returned boolean is serialized as a response body
    public ResponseEntity<Boolean> checkIdcardAvailability(@RequestParam("idcard") long idcard) {
        try {
            if (!staffService.checkIdcard(idcard)) {
                return ResponseEntity.ok(true); // idcard is available
            } else {
                return ResponseEntity.ok(false); // idcard is not available
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
}
