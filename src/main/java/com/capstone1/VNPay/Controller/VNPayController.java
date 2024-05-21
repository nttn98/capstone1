package com.capstone1.VNPay.Controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.capstone1.VNPay.Config.VNPayService;
import com.capstone1.model.Cart;
import com.capstone1.model.CartItem;
import com.capstone1.model.Order;
import com.capstone1.model.Order.PaymentType;
import com.capstone1.model.OrderDetail;
import com.capstone1.model.Product;
import com.capstone1.model.User;
import com.capstone1.services.CartItemService;
import com.capstone1.services.CartService;
import com.capstone1.services.OrderDetailService;
import com.capstone1.services.OrderService;
import com.capstone1.services.ProductService;
import com.capstone1.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@org.springframework.stereotype.Controller
public class VNPayController {
    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @PostMapping("/submitOrder")
    public String submidOrder(@RequestParam("amount") int orderTotal, @RequestParam("orderInfo") String orderInfo,
            HttpServletRequest request, Model model, HttpSession session, @RequestParam String name,
            @RequestParam String address, @RequestParam long numberphone) {

        session.setAttribute("name", name);
        session.setAttribute("address", address);
        session.setAttribute("numberphone", numberphone);

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/vnpay-payment-return")
    public String GetMapping(Model model, HttpSession session, HttpServletRequest request) {
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        String name = (String) session.getAttribute("name");
        String address = (String) session.getAttribute("address");
        Long numberphone = (Long) session.getAttribute("numberphone");

        model.addAttribute("name", name);
        model.addAttribute("address", address);
        model.addAttribute("numberphone", numberphone);
        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        if (paymentStatus == 1) {
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

                    CartItem temp = cartItemService.findByProductId(product.getId());

                    // change quantity after order
                    Product productAfter = productService.getProductById(product.getId());
                    productAfter.setQuantity(productAfter.getQuantity() - temp.getQuantity());

                    cartItemService.deleteByProductIdAndCartId(product.getId(), cart.getId());
                    total += tempOrderDetail.getFinalPrice();
                }
                order.setReceiverName(name);
                order.setReceiverAddress(address);
                order.setReceiverNumberphone(numberphone);
                order.setType(PaymentType.CREDIT);
                order.setTotal(total);
                cartService.deleteByUserId(userLogin.getId());
                cart.getListItem().clear();

            }
            session.removeAttribute("cart");
            orderService.saveOrder(order);
        }
        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
    }
}
