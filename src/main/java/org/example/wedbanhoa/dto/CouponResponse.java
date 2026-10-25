package org.example.wedbanhoa.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponResponse {

    private Integer couponId;
    private String code;
    private Integer discountPercent;
    private LocalDate expiryDate;
    private Integer status;
    private Double minOrderValue;

    // Alias for template (${c.id})
    public Integer getId() { return couponId; }
}
