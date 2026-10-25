package org.example.wedbanhoa.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OderResponse {

    private Integer orderId;
    private Integer userId;
    private Double totalPrice;
    private String status;
    private LocalDateTime orderDate;
    private String senderName;
    private String senderPhone;
    private String senderEmail;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String receiverCity;
    private String receiverDistrict;
    private String deliveryDate;
    private String deliveryTime;
    private String cardMessage;
    private Double vatAmount;
    private String specialRequests;
    private String couponCode;
    private Double discountAmount;
    private String paymentMethod;

    // Legacy template aliases (snake_case)
    public Integer getOrder_id() { return orderId; }
    public LocalDateTime getOrder_date() { return orderDate; }
    public String getSender_name() { return senderName; }
    public String getSender_phone() { return senderPhone; }
    public String getSender_email() { return senderEmail; }
    public String getReceiver_name() { return receiverName; }
    public String getReceiver_phone() { return receiverPhone; }
    public String getReceiver_address() { return receiverAddress; }
    public String getReceiver_city() { return receiverCity; }
    public String getReceiver_district() { return receiverDistrict; }
    public String getDelivery_date() { return deliveryDate; }
    public String getDelivery_time() { return deliveryTime; }
    public String getCard_message() { return cardMessage; }
    public String getSpecial_requests() { return specialRequests; }
    public String getCoupon_code() { return couponCode; }
    public Double getDiscount_amount() { return discountAmount; }
    public Double getVat_amount() { return vatAmount; }
    public Double getTotal_price() { return totalPrice; }
    public String getPayment_method() { return paymentMethod; }
}
