package org.example.wedbanhoa.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "status", columnDefinition = "NVARCHAR(50)")
    private String status;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "sender_name", columnDefinition = "NVARCHAR(255)")
    private String senderName;

    @Column(name = "sender_phone", length = 20)
    private String senderPhone;

    @Column(name = "sender_email", length = 255)
    private String senderEmail;

    @Column(name = "receiver_name", columnDefinition = "NVARCHAR(255)")
    private String receiverName;

    @Column(name = "receiver_phone", length = 20)
    private String receiverPhone;

    @Column(name = "receiver_address", columnDefinition = "NVARCHAR(500)")
    private String receiverAddress;

    @Column(name = "receiver_city", columnDefinition = "NVARCHAR(150)")
    private String receiverCity;

    @Column(name = "receiver_district", columnDefinition = "NVARCHAR(150)")
    private String receiverDistrict;

    @Column(name = "delivery_date", length = 50)
    private String deliveryDate;

    @Column(name = "delivery_time", columnDefinition = "NVARCHAR(100)")
    private String deliveryTime;

    @Column(name = "card_message", columnDefinition = "NVARCHAR(1000)")
    private String cardMessage;

    @Column(name = "vat_amount")
    private Double vatAmount;

    @Column(name = "special_requests", columnDefinition = "NVARCHAR(1000)")
    private String specialRequests;

    @Column(name = "coupon_code", length = 50)
    private String couponCode;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(name = "payment_method", columnDefinition = "NVARCHAR(100)")
    private String paymentMethod;
}
