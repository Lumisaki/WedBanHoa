package org.example.wedbanhoa.controller;

import jakarta.servlet.http.HttpSession;
import org.example.wedbanhoa.dto.CouponResponse;
import org.example.wedbanhoa.dto.OderRequest;
import org.example.wedbanhoa.dto.OderResponse;
import org.example.wedbanhoa.dto.ProductResponse;
import org.example.wedbanhoa.entity.Account;
import org.example.wedbanhoa.service.CouponService;
import org.example.wedbanhoa.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class CartController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private org.example.wedbanhoa.service.BankSettingService bankSettingService;

    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        setHeaderData(model, session);

        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        List<ProductResponse> cartList = new ArrayList<>();
        double totalAmount = 0.0;

        if (cart != null && !cart.isEmpty()) {
            Map<Integer, ProductResponse> products = productService.getProductsForCart(cart.keySet());
            for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                ProductResponse p = products.get(entry.getKey());
                if (p != null) {
                    p.setQuantity(entry.getValue());
                    totalAmount += p.getFinalPrice() * entry.getValue();
                    cartList.add(p);
                }
            }
        }
        model.addAttribute("cartList", cartList);
        model.addAttribute("totalAmount", totalAmount);
        return "view/cart";
    }

    // Hỗ trợ cả GET và POST cho AddToCart (index / productDetail dùng thẻ <a> lẫn <form>)
    @RequestMapping(value = "/AddToCart", method = {RequestMethod.GET, RequestMethod.POST})
    public String addToCart(@RequestParam(value = "id", required = false) Integer id,
                            @RequestParam(value = "pid", required = false) Integer pid,
                            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                            @RequestParam(value = "action", required = false) String action,
                            HttpSession session) {
        Integer targetPid = pid != null ? pid : id;
        if (targetPid == null) return "redirect:/index";

        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        cart.put(targetPid, cart.getOrDefault(targetPid, 0) + quantity);

        if ("buynow".equalsIgnoreCase(action)) {
            return "redirect:/checkout";
        }
        return "redirect:/cart";
    }

    // Hỗ trợ cả GET và POST cho UpdateCart (cart.html dùng link URL ?pid=...&action=inc/dec)
    @RequestMapping(value = "/UpdateCart", method = {RequestMethod.GET, RequestMethod.POST})
    public String updateCart(@RequestParam("pid") Integer pid,
                             @RequestParam(value = "quantity", required = false) Integer quantity,
                             @RequestParam(value = "action", required = false) String action,
                             HttpSession session) {
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        if (cart != null && cart.containsKey(pid)) {
            int currentQty = cart.getOrDefault(pid, 1);
            if ("inc".equalsIgnoreCase(action)) {
                cart.put(pid, currentQty + 1);
            } else if ("dec".equalsIgnoreCase(action)) {
                if (currentQty <= 1) {
                    cart.remove(pid);
                } else {
                    cart.put(pid, currentQty - 1);
                }
            } else if (quantity != null) {
                if (quantity <= 0) {
                    cart.remove(pid);
                } else {
                    cart.put(pid, quantity);
                }
            }
        }
        return "redirect:/cart";
    }

    @GetMapping("/RemoveFromCart")
    public String removeFromCart(@RequestParam("pid") Integer pid,
                                 @RequestParam(value = "from", defaultValue = "cart") String from,
                                 HttpSession session) {
        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        if (cart != null) {
            cart.remove(pid);
        }
        return "redirect:/" + ("checkout".equalsIgnoreCase(from) ? "checkout" : "cart");
    }

    @GetMapping("/checkout")
    public String checkoutPage(Model model, HttpSession session) {
        Account user = (Account) session.getAttribute("userSession");
        if (user == null) {
            return "redirect:/Login";
        }

        setHeaderData(model, session);

        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        List<ProductResponse> cartList = new ArrayList<>();
        double subTotal = 0.0;

        if (cart != null && !cart.isEmpty()) {
            Map<Integer, ProductResponse> products = productService.getProductsForCart(cart.keySet());
            for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                ProductResponse p = products.get(entry.getKey());
                if (p != null) {
                    p.setQuantity(entry.getValue());
                    subTotal += p.getFinalPrice() * entry.getValue();
                    cartList.add(p);
                }
            }
        }

        double vat = subTotal * 0.08;
        double grandTotal = subTotal + vat;

        model.addAttribute("cartList", cartList);
        model.addAttribute("subTotal", subTotal);
        model.addAttribute("vat", vat);
        model.addAttribute("grandTotal", grandTotal);
        return "view/checkout";
    }

    @PostMapping("/PlaceOrder")
    public String placeOrder(
            @RequestParam(value = "sender_name", required = false) String sNameSnake,
            @RequestParam(value = "senderName", required = false) String sNameCamel,
            @RequestParam(value = "sender_phone", required = false) String sPhoneSnake,
            @RequestParam(value = "senderPhone", required = false) String sPhoneCamel,
            @RequestParam(value = "sender_email", required = false) String sEmailSnake,
            @RequestParam(value = "senderEmail", required = false) String sEmailCamel,
            @RequestParam(value = "receiver_name", required = false) String rNameSnake,
            @RequestParam(value = "receiverName", required = false) String rNameCamel,
            @RequestParam(value = "receiver_phone", required = false) String rPhoneSnake,
            @RequestParam(value = "receiverPhone", required = false) String rPhoneCamel,
            @RequestParam(value = "receiver_address", required = false) String rAddressSnake,
            @RequestParam(value = "receiverAddress", required = false) String rAddressCamel,
            @RequestParam(value = "receiver_city", required = false) String rCitySnake,
            @RequestParam(value = "receiverCity", required = false) String rCityCamel,
            @RequestParam(value = "receiver_district", required = false) String rDistrictSnake,
            @RequestParam(value = "receiverDistrict", required = false) String rDistrictCamel,
            @RequestParam(value = "delivery_date", required = false) String delivDateSnake,
            @RequestParam(value = "deliveryDate", required = false) String delivDateCamel,
            @RequestParam(value = "delivery_time", required = false) String delivTimeSnake,
            @RequestParam(value = "deliveryTime", required = false) String delivTimeCamel,
            @RequestParam(value = "card_message", required = false) String cardMsgSnake,
            @RequestParam(value = "cardMessage", required = false) String cardMsgCamel,
            @RequestParam(value = "special_requests", required = false) String specialReqSnake,
            @RequestParam(value = "specialRequests", required = false) String specialReqCamel,
            @RequestParam(value = "paymentMethod", required = false, defaultValue = "COD") String paymentMethod,
            @RequestParam(value = "payment_method", required = false) String paymentMethodSnake,
            @RequestParam(value = "couponCode", required = false) String couponCodeCamel,
            @RequestParam(value = "applied_coupon", required = false) String couponCodeSnake,
            HttpSession session) {

        Account user = (Account) session.getAttribute("userSession");
        if (user == null) return "redirect:/Login";

        @SuppressWarnings("unchecked")
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) return "redirect:/cart";

        String sName = sNameSnake != null && !sNameSnake.isEmpty() ? sNameSnake : (sNameCamel != null ? sNameCamel : user.getFullname());
        String sPhone = sPhoneSnake != null && !sPhoneSnake.isEmpty() ? sPhoneSnake : (sPhoneCamel != null ? sPhoneCamel : user.getPhone());
        String sEmail = sEmailSnake != null && !sEmailSnake.isEmpty() ? sEmailSnake : (sEmailCamel != null ? sEmailCamel : user.getEmail());

        String rName = rNameSnake != null && !rNameSnake.isEmpty() ? rNameSnake : (rNameCamel != null ? rNameCamel : sName);
        String rPhone = rPhoneSnake != null && !rPhoneSnake.isEmpty() ? rPhoneSnake : (rPhoneCamel != null ? rPhoneCamel : sPhone);
        String rAddress = rAddressSnake != null && !rAddressSnake.isEmpty() ? rAddressSnake : rAddressCamel;
        String rCity = rCitySnake != null && !rCitySnake.isEmpty() ? rCitySnake : rCityCamel;
        String rDistrict = rDistrictSnake != null && !rDistrictSnake.isEmpty() ? rDistrictSnake : rDistrictCamel;
        String delivDate = delivDateSnake != null && !delivDateSnake.isEmpty() ? delivDateSnake : delivDateCamel;
        String delivTime = delivTimeSnake != null && !delivTimeSnake.isEmpty() ? delivTimeSnake : delivTimeCamel;
        String cardMsg = cardMsgSnake != null ? cardMsgSnake : cardMsgCamel;
        String specialReq = specialReqSnake != null ? specialReqSnake : specialReqCamel;
        String finalPaymentMethod = paymentMethodSnake != null && !paymentMethodSnake.isEmpty() ? paymentMethodSnake : paymentMethod;
        String couponCode = couponCodeSnake != null && !couponCodeSnake.isEmpty() ? couponCodeSnake : couponCodeCamel;

        double subTotal = 0.0;
        Map<Integer, ProductResponse> products = productService.getProductsForCart(cart.keySet());
        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            ProductResponse p = products.get(entry.getKey());
            if (p != null) {
                subTotal += p.getFinalPrice() * entry.getValue();
            }
        }

        double discountAmount = 0.0;
        if (couponCode != null && !couponCode.trim().isEmpty()) {
            CouponResponse c = couponService.getByCode(couponCode.trim());
            if (c != null && c.getStatus() != null && c.getStatus() == 1) {
                discountAmount = subTotal * (c.getDiscountPercent() / 100.0);
            }
        }

        double vat = (subTotal - discountAmount) * 0.08;
        double grandTotal = Math.max(0, subTotal - discountAmount + vat);

        boolean isBankTransfer = "BankTransfer".equalsIgnoreCase(finalPaymentMethod) || "Bank Transfer".equalsIgnoreCase(finalPaymentMethod) || "Online".equalsIgnoreCase(finalPaymentMethod);
        String initialStatus = isBankTransfer ? "Waiting Payment" : "Pending";
        String normalizedPaymentMethod = isBankTransfer ? "Bank Transfer" : "COD";

        OderRequest orderRequest = OderRequest.builder()
                .userId(user.getUserId())
                .senderName(sName)
                .senderPhone(sPhone)
                .senderEmail(sEmail)
                .receiverName(rName)
                .receiverPhone(rPhone)
                .receiverAddress(rAddress)
                .receiverCity(rCity)
                .receiverDistrict(rDistrict)
                .deliveryDate(delivDate)
                .deliveryTime(delivTime)
                .cardMessage(cardMsg)
                .specialRequests(specialReq)
                .paymentMethod(normalizedPaymentMethod)
                .couponCode(couponCode)
                .discountAmount(discountAmount)
                .vatAmount(vat)
                .totalPrice(grandTotal)
                .build();

        OderResponse saved = orderService.placeOrderFromRequest(orderRequest, cart);
        orderService.confirmPaymentAndDeductStock(saved.getOrderId(), normalizedPaymentMethod, initialStatus);

        session.removeAttribute("cart");

        if (isBankTransfer) {
            session.setAttribute("successMsg", "Đơn hàng #" + saved.getOrderId() + " đã được tạo! Vui lòng hoàn tất chuyển khoản theo hướng dẫn bên dưới.");
            return "redirect:/PaymentTransfer?orderId=" + saved.getOrderId();
        } else {
            session.setAttribute("successMsg", "Đặt hàng thành công với hình thức Thanh toán khi nhận hàng (COD)! Mã đơn #" + saved.getOrderId());
            return "redirect:/OrderDetail?orderId=" + saved.getOrderId();
        }
    }

    @GetMapping({"/PaymentTransfer", "/payment"})
    public String paymentTransferPage(@RequestParam("orderId") Integer orderId, Model model, HttpSession session) {
        Account user = (Account) session.getAttribute("userSession");
        if (user == null) return "redirect:/Login";
        setHeaderData(model, session);

        OderResponse order = orderService.getOrderById(orderId);
        if (order == null) return "redirect:/index";

        model.addAttribute("order", order);
        model.addAttribute("bankSetting", bankSettingService.getBankSetting());
        return "view/payment";
    }
}
