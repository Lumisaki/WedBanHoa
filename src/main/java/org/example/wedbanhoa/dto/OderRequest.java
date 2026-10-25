package org.example.wedbanhoa.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OderRequest {

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
}
