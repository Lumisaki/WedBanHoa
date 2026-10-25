package org.example.wedbanhoa.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OderDetailRequest {

    private Integer detailId;
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
    private Double price;
}
