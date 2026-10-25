package org.example.wedbanhoa.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OderDetailResponse {

    private Integer detailId;
    private Integer orderId;
    private Integer productId;
    private String productName;
    private String productImage;
    private Integer quantity;
    private Double price;

    public Double getSubTotal() {
        if (price == null || quantity == null) return 0.0;
        return price * quantity;
    }

    // Aliases for legacy templates
    public String getProduct_name() { return productName; }
    public String getImage() { return productImage; }
    public Integer getProduct_id() { return productId; }
}
