package org.example.wedbanhoa.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponRequest {

    private Integer couponId;

    @NotBlank(message = "Mã giảm giá không được để trống")
    private String code;

    @NotNull(message = "Phần trăm giảm giá không được để trống")
    @Min(value = 1, message = "Giảm tối thiểu 1%")
    @Max(value = 100, message = "Giảm tối đa 100%")
    private Integer discountPercent;

    private LocalDate expiryDate;

    private Integer status;

    private Double minOrderValue;

    // Aliases
    public Integer getId() { return couponId; }
    public void setId(Integer id) { this.couponId = id; }
}
