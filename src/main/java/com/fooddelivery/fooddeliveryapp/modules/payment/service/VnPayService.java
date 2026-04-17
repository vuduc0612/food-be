package com.fooddelivery.fooddeliveryapp.modules.payment.service;

import com.fooddelivery.fooddeliveryapp.components.VNPayConfig;
import com.fooddelivery.fooddeliveryapp.exception.DataNotFoundException;
import com.fooddelivery.fooddeliveryapp.modules.cart.dto.CartDto;
import com.fooddelivery.fooddeliveryapp.modules.cart.entity.Cart;
import com.fooddelivery.fooddeliveryapp.modules.cart.service.CartService;
import com.fooddelivery.fooddeliveryapp.modules.order.entity.Order;
import com.fooddelivery.fooddeliveryapp.modules.order.service.OrderService;
import com.fooddelivery.fooddeliveryapp.modules.user.entity.User;
import com.fooddelivery.fooddeliveryapp.modules.user.repository.UserRepository;
import com.fooddelivery.fooddeliveryapp.utils.VNPayUtils;
import com.fooddelivery.fooddeliveryapp.modules.order.repository.OrderRepository;
import com.fooddelivery.fooddeliveryapp.modules.payment.dto.PaymentDto;
import com.fooddelivery.fooddeliveryapp.modules.payment.dto.PaymentResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class VnPayService {
    private static final Logger log = LoggerFactory.getLogger(VnPayService.class);

    private final OrderRepository orderRepository;
    private final VNPayConfig vnPayConfig;
    private final CartService cartService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final OrderService orderService;
    private final Executor orderTaskExecutor;

    public VnPayService(
            OrderRepository orderRepository,
            VNPayConfig vnPayConfig,
            CartService cartService,
            UserRepository userRepository,
            ModelMapper modelMapper,
            OrderService orderService,
            @Qualifier("orderTaskExecutor") Executor orderTaskExecutor) {
        this.orderRepository = orderRepository;
        this.vnPayConfig = vnPayConfig;
        this.cartService = cartService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.orderService = orderService;
        this.orderTaskExecutor = orderTaskExecutor;
    }

    public String createPaymentUrl(PaymentDto paymentDto, HttpServletRequest request) {
        System.out.println("Payment DTO: " + paymentDto);
        long amount = paymentDto.getAmount() * 100; //Integer.parseInt(request.getParameter("amount")) * 100L;
        String bankCode = paymentDto.getBankCode();
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig(paymentDto.getOrderId());
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        System.out.println("Bank code: " + bankCode);
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtils.getIpAddress(request));

        String hashData = VNPayUtils.getPaymentURL(vnpParamsMap, false);
        String queryData = VNPayUtils.getPaymentURL(vnpParamsMap, true);

        String secureHash = VNPayUtils.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryData = queryData + "&vnp_SecureHash=" + secureHash;
        String paymentUrl = vnPayConfig.getVnpPayUrl() + "?" + queryData;

//        System.out.println("========== VNPAY DEBUG ==========");
//        System.out.println("Params:");
//        for (Map.Entry<String, String> entry : vnpParamsMap.entrySet()) {
//            System.out.println(entry.getKey() + " = " + entry.getValue());
//        }
//        System.out.println("Hash data = " + hashData);
//        System.out.println("Secure hash = " + secureHash);
//        System.out.println("Return URL raw: [" + vnPayConfig.getVnpReturnUrl() + "]");
//        System.out.println("Return secret key: [" + vnPayConfig.getSecretKey() + "]");
//        System.out.println("Full payment URL = " + vnPayConfig.getVnpPayUrl() + "?" + queryData);
//        System.out.println("=================================");

        return paymentUrl;
    }

    public PaymentResponse processPaymentCallback(Map<String, String> vnp_Params) throws Exception {
        // Mã đơn hàng
        String vnp_TxnRef = vnp_Params.get("vnp_TxnRef");
        // Mã giao dịch tại VNPAY
        String vnp_TransactionNo = vnp_Params.get("vnp_TransactionNo");
        // Mã phản hồi từ VNPAY
        String vnp_ResponseCode = vnp_Params.get("vnp_ResponseCode");
        // Mã ngân hàng
        String vnp_BankCode = vnp_Params.get("vnp_BankCode");

        String vnpOrderInfo = vnp_Params.get("vnp_OrderInfo");
        log.info("[paymentCallback] vnpOrderInfo: {}", vnpOrderInfo);
        String[] parts = vnpOrderInfo.split(":");
        Long orderId = Long.parseLong(parts[1]);

        log.info("[paymentCallback] Order ID: {}", orderId);
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setTransactionId(vnp_TransactionNo);

        // Kiểm tra chữ ký và mã phản hồi
        if (validateCallback(vnp_Params)) {
            if ("00".equals(vnp_ResponseCode)) {
                // Thanh toán thành công
                log.info("[paymentCallback] Thanh toán thành công - thread: {}", Thread.currentThread().getName());
                paymentResponse.setSuccess(true);
                paymentResponse.setMessage(getResponseDescription(vnp_ResponseCode));

                // Query order 1 lần, dùng chung cho cả 2 task
                Order order = orderRepository.findById(orderId)
                        .orElseThrow(() -> new DataNotFoundException("Order not found"));

                // Idempotency: Nếu đã thanh toán rồi thì bỏ qua (tránh duplicate callback)
                if (Boolean.TRUE.equals(order.getIsPaid())) {
                    log.info("[paymentCallback] Order {} đã được thanh toán trước đó, bỏ qua", orderId);
                    paymentResponse.setSuccess(true);
                    paymentResponse.setMessage("Đơn hàng đã được xử lý");
                    return paymentResponse;
                }

                // Song song: Cập nhật order + Xóa cart chạy đồng thời
                CompletableFuture<Void> updateOrderFuture = CompletableFuture.runAsync(() -> {
                    log.info("[paymentCallback] Cập nhật order - thread: {}", Thread.currentThread().getName());
                    order.setTransactionId(vnp_TxnRef);
                    order.setIsPaid(true);
                    orderRepository.save(order);
                }, orderTaskExecutor);

                CompletableFuture<Void> clearCartFuture = CompletableFuture.runAsync(() -> {
                    log.info("[paymentCallback] Xóa giỏ hàng - thread: {}", Thread.currentThread().getName());
                    User user = userRepository.findById(order.getUser().getId())
                            .orElseThrow(() -> new DataNotFoundException("User not found"));
                    CartDto cart = cartService.getCart(user.getId());
                    cartService.clearCart(user.getId(), modelMapper.map(cart, Cart.class));
                }, orderTaskExecutor);

                // Chờ cả hai hoàn thành
                CompletableFuture.allOf(updateOrderFuture, clearCartFuture).join();
                log.info("[paymentCallback] Hoàn thành cập nhật order + xóa cart");

            } else {
                // Thanh toán thất bại
                orderService.deleteOrder(orderId);
                paymentResponse.setSuccess(false);
                paymentResponse.setMessage(getResponseDescription(vnp_ResponseCode));
            }
        } else {
            // Chữ ký không hợp lệ
            paymentResponse.setSuccess(false);
            paymentResponse.setMessage("Dữ liệu không hợp lệ");
        }

        return paymentResponse;
    }

    public boolean validateCallback(Map<String, String> vnp_Params) throws Exception {
        String vnp_SecureHash = vnp_Params.get("vnp_SecureHash");
        // Xóa chữ ký khỏi danh sách tham số để tính toán
        if (vnp_Params.containsKey("vnp_SecureHash")) {
            vnp_Params.remove("vnp_SecureHash");
        }

        if (vnp_Params.containsKey("vnp_SecureHashType")) {
            vnp_Params.remove("vnp_SecureHashType");
        }

        // Sắp xếp các tham số theo thứ tự a-z
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        // Tạo chuỗi hash data
        String hashData = VNPayUtils.getPaymentURL(vnp_Params, false);
        String calculatedHash = VNPayUtils.hmacSHA512(vnPayConfig.getSecretKey(), hashData);

        return calculatedHash.equals(vnp_SecureHash);
    }


    private String getResponseDescription(String responseCode) {
        switch (responseCode) {
            case "00":
                return "Giao dịch thành công";
            case "07":
                return "Trừ tiền thành công, giao dịch bị nghi ngờ";
            case "09":
                return "Giao dịch không thành công do: Thẻ/Tài khoản không đủ số dư";
            case "10":
                return "Giao dịch không thành công do: Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần";
            case "11":
                return "Giao dịch không thành công do: Đã hết hạn chờ thanh toán";
            case "12":
                return "Giao dịch không thành công do: Thẻ/Tài khoản bị khóa";
            case "24":
                return "Giao dịch không thành công do: Khách hàng hủy giao dịch";
            default:
                return "Lỗi giao dịch";
        }
    }

}
