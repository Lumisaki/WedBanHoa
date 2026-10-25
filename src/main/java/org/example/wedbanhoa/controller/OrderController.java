package org.example.wedbanhoa.controller;

import jakarta.servlet.http.HttpSession;
import org.example.wedbanhoa.dto.OderDetailResponse;
import org.example.wedbanhoa.dto.OderResponse;
import org.example.wedbanhoa.entity.Account;
import org.example.wedbanhoa.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/OrderDetail")
    public String orderDetail(@RequestParam("orderId") Integer orderId, Model model, HttpSession session) {
        Account user = (Account) session.getAttribute("userSession");
        if (user == null) return "redirect:/Login";
        setHeaderData(model, session);

        OderResponse order = orderService.getOrderById(orderId);
        if (order == null) {
            return "redirect:/profilm";
        }

        // Kiểm tra quyền: chỉ admin hoặc chính chủ đơn hàng mới được xem
        if (user.getRoleId() != 1 && !order.getUserId().equals(user.getUserId())) {
            return "redirect:/profilm";
        }

        List<OderDetailResponse> items = orderService.getOrderDetails(orderId);
        double originalSubTotal = items.stream().mapToDouble(OderDetailResponse::getSubTotal).sum();

        boolean isEditable = "Pending".equalsIgnoreCase(order.getStatus());

        model.addAttribute("order", order);
        model.addAttribute("items", items);
        model.addAttribute("isEditable", isEditable);
        model.addAttribute("originalSubTotal", originalSubTotal);

        return "view/orderDetail";
    }

    @PostMapping("/CancelOrder")
    public String cancelOrder(@RequestParam("orderId") Integer orderId, HttpSession session) {
        Account user = (Account) session.getAttribute("userSession");
        if (user == null) return "redirect:/Login";

        boolean ok = orderService.cancelOrder(orderId, user.getUserId());
        if (ok) {
            session.setAttribute("message", "Đã hủy đơn hàng #" + orderId + " thành công!");
        } else {
            session.setAttribute("error", "Không thể hủy đơn hàng này!");
        }
        return "redirect:/OrderDetail?orderId=" + orderId;
    }

    @PostMapping("/ReOrder")
    public String reOrder(@RequestParam("orderId") Integer orderId, HttpSession session) {
        Account user = (Account) session.getAttribute("userSession");
        if (user == null) return "redirect:/Login";

        List<OderDetailResponse> items = orderService.getOrderDetails(orderId);
        for (OderDetailResponse item : items) {
            cartControllerAddToCart(item.getProductId(), item.getQuantity(), session);
        }

        session.setAttribute("successMsg", "Đã nạp lại các sản phẩm vào giỏ hàng!");
        return "redirect:/cart";
    }

    @Autowired
    private org.example.wedbanhoa.service.ReviewService reviewService;

    @PostMapping("/SubmitReview")
    public String submitReview(@RequestParam("orderId") Integer orderId,
                               @RequestParam("productId") Integer productId,
                               @RequestParam("rating") Integer rating,
                               @RequestParam("comment") String comment,
                               HttpSession session) {
        Account user = (Account) session.getAttribute("userSession");
        if (user == null) return "redirect:/Login";

        org.example.wedbanhoa.dto.ReviewRequest reviewRequest = org.example.wedbanhoa.dto.ReviewRequest.builder()
                .userId(user.getUserId())
                .productId(productId)
                .rating(rating != null ? rating : 5)
                .comment(comment != null ? comment.trim() : "")
                .build();
        reviewService.saveReview(reviewRequest);

        session.setAttribute("message", "Cảm ơn bạn đã đánh giá món hoa!");
        return "redirect:/OrderDetail?orderId=" + orderId;
    }

    @PostMapping("/UpdateOrderDetails")
    public String updateOrderDetails(@RequestParam("orderId") Integer orderId,
                                     @RequestParam(value = "receiver_name", required = false) String receiverName,
                                     @RequestParam(value = "receiver_phone", required = false) String receiverPhone,
                                     @RequestParam(value = "receiver_address", required = false) String receiverAddress,
                                     @RequestParam(value = "receiver_district", required = false) String receiverDistrict,
                                     @RequestParam(value = "receiver_city", required = false) String receiverCity,
                                     @RequestParam(value = "delivery_date", required = false) String deliveryDate,
                                     @RequestParam(value = "delivery_time", required = false) String deliveryTime,
                                     @RequestParam(value = "card_message", required = false) String cardMessage,
                                     @RequestParam(value = "special_requests", required = false) String specialRequests,
                                     HttpSession session) {
        Account user = (Account) session.getAttribute("userSession");
        if (user == null) return "redirect:/Login";

        OderResponse order = orderService.getOrderById(orderId);
        if (order != null && "Pending".equalsIgnoreCase(order.getStatus()) && order.getUserId().equals(user.getUserId())) {
            var entity = orderService.getOrderEntityById(orderId);
            if (entity != null) {
                if (receiverName != null) entity.setReceiverName(receiverName);
                if (receiverPhone != null) entity.setReceiverPhone(receiverPhone);
                if (receiverAddress != null) entity.setReceiverAddress(receiverAddress);
                if (receiverDistrict != null) entity.setReceiverDistrict(receiverDistrict);
                if (receiverCity != null) entity.setReceiverCity(receiverCity);
                if (deliveryDate != null) entity.setDeliveryDate(deliveryDate);
                if (deliveryTime != null) entity.setDeliveryTime(deliveryTime);
                if (cardMessage != null) entity.setCardMessage(cardMessage);
                if (specialRequests != null) entity.setSpecialRequests(specialRequests);
                orderService.placeOrder(entity, java.util.Collections.emptyMap());
            }
            session.setAttribute("message", "Cập nhật thông tin giao hoa thành công!");
        }
        return "redirect:/OrderDetail?orderId=" + orderId;
    }

    @SuppressWarnings("unchecked")
    private void cartControllerAddToCart(Integer pid, Integer qty, HttpSession session) {
        var cart = (java.util.Map<Integer, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new java.util.HashMap<>();
            session.setAttribute("cart", cart);
        }
        cart.put(pid, cart.getOrDefault(pid, 0) + qty);
    }
}
