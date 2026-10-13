package org.example.wedbanhoa.service;

import org.example.wedbanhoa.dto.OderDetailRequest;
import org.example.wedbanhoa.dto.OderDetailResponse;
import org.example.wedbanhoa.dto.OderRequest;
import org.example.wedbanhoa.dto.OderResponse;
import org.example.wedbanhoa.entity.Order;
import org.example.wedbanhoa.entity.OrderDetail;
import org.example.wedbanhoa.entity.Product;
import org.example.wedbanhoa.repository.OrderDetailRepository;
import org.example.wedbanhoa.repository.OrderRepository;
import org.example.wedbanhoa.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    public long getTotalOrders() {
        return orderRepository.countValidOrders();
    }

    public Double getTotalRevenue() {
        return orderRepository.getTotalRevenue();
    }

    public Double getTotalSpentByUser(Integer userId) {
        return orderRepository.getTotalSpentByUser(userId);
    }

    @Transactional(readOnly = true)
    public List<OderResponse> getOrderHistoryByUserId(Integer userId) {
        return orderRepository.findByUserIdAndStatusNotOrderByOrderDateDesc(userId, "Unpaid").stream()
                .map(this::toResponse) // Gọi hàm mapping nội bộ
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OderResponse> getAllOrdersForAdmin() {
        return orderRepository.findByStatusNotOrderByOrderDateDesc("Unpaid").stream()
                .map(this::toResponse) // Gọi hàm mapping nội bộ
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<OderResponse> getAllOrdersForAdminPaged(int page, int pageSize) {
        Page<Order> orderPage = orderRepository.findByStatusNotOrderByOrderDateDesc("Unpaid", PageRequest.of(page - 1, pageSize));
        List<OderResponse> dtoList = orderPage.getContent().stream()
                .map(this::toResponse) // Gọi hàm mapping nội bộ
                .toList();
        return new PageImpl<>(dtoList, orderPage.getPageable(), orderPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public OderResponse getOrderById(Integer orderId) {
        return orderRepository.findById(orderId)
                .map(this::toResponse) // Gọi hàm mapping nội bộ
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public Order getOrderEntityById(Integer orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<OderDetailResponse> getOrderDetails(Integer orderId) {
        List<OrderDetail> details = orderDetailRepository.findByOrderId(orderId);
        return details.stream().map(d -> {
            String pName = "";
            String pImage = "";
            if (d.getProductId() != null) {
                Product p = productRepository.findById(d.getProductId()).orElse(null);
                if (p != null) {
                    pName = p.getProductName();
                    pImage = p.getImage();
                }
            }
            return toDetailResponse(d, pName, pImage); // Gọi hàm mapping nội bộ
        }).toList();
    }

    @Transactional
    public Order placeOrder(Order order, Map<Integer, Integer> cart) {
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Unpaid");
        Order savedOrder = orderRepository.save(order);

        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            Integer productId = entry.getKey();
            Integer qty = entry.getValue();
            Product p = productRepository.findById(productId).orElse(null);
            if (p != null) {
                double finalPrice = p.getPrice();
                if (p.getDiscount() != null && p.getDiscount() > 0) {
                    finalPrice = p.getPrice() * (1 - p.getDiscount() / 100.0); // Đã fix cú pháp tính discount
                }
                OrderDetail detail = OrderDetail.builder()
                        .orderId(savedOrder.getOrderId())
                        .productId(productId)
                        .quantity(qty)
                        .price(finalPrice)
                        .build();
                orderDetailRepository.save(detail);
            }
        }
        return savedOrder;
    }

    @Transactional
    public OderResponse placeOrderFromRequest(OderRequest request, Map<Integer, Integer> cart) {
        Order order = toEntity(request); // Gọi hàm mapping nội bộ
        Order saved = placeOrder(order, cart);
        return toResponse(saved); // Gọi hàm mapping nội bộ
    }

    @Transactional
    public void confirmPaymentAndDeductStock(Integer orderId, String paymentMethod, String status) {
        Order order = getOrderEntityById(orderId);
        if (order != null) {
            order.setPaymentMethod(paymentMethod);
            order.setStatus(status);
            orderRepository.save(order);

            List<OrderDetail> details = orderDetailRepository.findByOrderId(orderId);
            for (OrderDetail d : details) {
                Product p = productRepository.findById(d.getProductId()).orElse(null);
                if (p != null && p.getStock() != null) {
                    p.setStock(Math.max(0, p.getStock() - d.getQuantity()));
                    productRepository.save(p);
                }
            }
        }
    }

    @Transactional
    public boolean updateStatus(Integer orderId, String status) {
        Order o = getOrderEntityById(orderId);
        if (o != null) {
            o.setStatus(status);
            orderRepository.save(o);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean cancelOrder(Integer orderId, Integer userId) {
        Order o = getOrderEntityById(orderId);
        if (o != null && o.getUserId() != null && o.getUserId().equals(userId) && "Pending".equalsIgnoreCase(o.getStatus())) {
            o.setStatus("Cancelled");
            orderRepository.save(o);
            return true;
        }
        return false;
    }

    // =========================================================================
    // CÁC HÀM MAPPING ĐƯỢC CHUYỂN TRỰC TIẾP VÀO ĐÂY
    // =========================================================================

    private Order toEntity(OderRequest request) {
        if (request == null) return null;
        return Order.builder()
                .orderId(request.getOrderId())
                .userId(request.getUserId())
                .totalPrice(request.getTotalPrice())
                .status(request.getStatus())
                .orderDate(request.getOrderDate())
                .senderName(request.getSenderName())
                .senderPhone(request.getSenderPhone())
                .senderEmail(request.getSenderEmail())
                .receiverName(request.getReceiverName())
                .receiverPhone(request.getReceiverPhone())
                .receiverAddress(request.getReceiverAddress())
                .receiverCity(request.getReceiverCity())
                .receiverDistrict(request.getReceiverDistrict())
                .deliveryDate(request.getDeliveryDate())
                .deliveryTime(request.getDeliveryTime())
                .cardMessage(request.getCardMessage())
                .vatAmount(request.getVatAmount())
                .specialRequests(request.getSpecialRequests())
                .couponCode(request.getCouponCode())
                .discountAmount(request.getDiscountAmount())
                .paymentMethod(request.getPaymentMethod())
                .build();
    }

    private OderResponse toResponse(Order entity) {
        if (entity == null) return null;
        return OderResponse.builder()
                .orderId(entity.getOrderId())
                .userId(entity.getUserId())
                .totalPrice(entity.getTotalPrice())
                .status(entity.getStatus())
                .orderDate(entity.getOrderDate())
                .senderName(entity.getSenderName())
                .senderPhone(entity.getSenderPhone())
                .senderEmail(entity.getSenderEmail())
                .receiverName(entity.getReceiverName())
                .receiverPhone(entity.getReceiverPhone())
                .receiverAddress(entity.getReceiverAddress())
                .receiverCity(entity.getReceiverCity())
                .receiverDistrict(entity.getReceiverDistrict())
                .deliveryDate(entity.getDeliveryDate())
                .deliveryTime(entity.getDeliveryTime())
                .cardMessage(entity.getCardMessage())
                .vatAmount(entity.getVatAmount())
                .specialRequests(entity.getSpecialRequests())
                .couponCode(entity.getCouponCode())
                .discountAmount(entity.getDiscountAmount())
                .paymentMethod(entity.getPaymentMethod())
                .build();
    }

    private OrderDetail toDetailEntity(OderDetailRequest request) {
        if (request == null) return null;
        return OrderDetail.builder()
                .detailId(request.getDetailId())
                .orderId(request.getOrderId())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .build();
    }

    private OderDetailResponse toDetailResponse(OrderDetail entity, String productName, String productImage) {
        if (entity == null) return null;
        return OderDetailResponse.builder()
                .detailId(entity.getDetailId())
                .orderId(entity.getOrderId())
                .productId(entity.getProductId())
                .productName(productName)
                .productImage(productImage)
                .quantity(entity.getQuantity())
                .price(entity.getPrice())
                .build();
    }
}