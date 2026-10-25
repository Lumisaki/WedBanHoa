package org.example.wedbanhoa.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Integer productId;
    private String productName;
    private String description;
    private Double price;
    private Integer stock;
    private String image;
    private Integer categoryId;
    private String categoryName;
    private Integer discount;
    private Integer sold;
    private Integer quantity;

    public Double getFinalPrice() {
        if (price == null) return 0.0;
        if (discount != null && discount > 0) {
            return price * (1 - discount / 100.0);
        }
        return price;
    }

    // Aliases for legacy JSP/Thymeleaf expression compatibility (${o.id}, ${o.pname}, etc.)
    public Integer getId() { return productId; }
    public String getPname() { return productName; }
    public String getPdescription() { return description; }
    public Double getPprice() { return price; }
    public Integer getPstock() { return stock; }
    public String getPimage() { return image; }
    public Integer getCateID() { return categoryId; }
}
