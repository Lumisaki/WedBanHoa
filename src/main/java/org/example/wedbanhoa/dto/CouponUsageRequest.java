package org.example.wedbanhoa.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponUsageRequest {
    private Integer usageId;
    private Integer couponId;
    private Integer userId;
    private Integer orderId;
    private LocalDateTime usedAt;
}
