package org.example.wedbanhoa.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponUsageResponse {
    private Integer usageId;
    private Integer couponId;
    private Integer userId;
    private Integer orderId;
    private LocalDateTime usedAt;
    private String userEmail;
    private String couponCode;
}
