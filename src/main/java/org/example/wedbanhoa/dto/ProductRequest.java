package org.example.wedbanhoa.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    private Integer productId;

    @NotBlank(message = "Tên sản phẩm hoa không được để trống")
    private String productName;

    private String description;

    @NotNull(message = "Giá gốc không được để trống")
    @PositiveOrZero(message = "Giá gốc phải lớn hơn hoặc bằng 0")
    private Double price;

    @NotNull(message = "Số lượng kho không được để trống")
    @Min(value = 0, message = "Số lượng kho không được âm")
    private Integer stock;

    private String image;

    private Integer categoryId;

    @Min(value = 0, message = "Giảm giá tối thiểu là 0%")
    private Integer discount;

    private MultipartFile imageFile;
    private String fromPage;

    // Helper getters/setters for legacy form binding compatibility
    public Integer getPid() { return productId; }
    public void setPid(Integer pid) { this.productId = pid; }

    public String getPname() { return productName; }
    public void setPname(String pname) { this.productName = pname; }

    public String getPdescription() { return description; }
    public void setPdescription(String pdescription) { this.description = pdescription; }

    public Double getPprice() { return price; }
    public void setPprice(Double pprice) { this.price = pprice; }

    public Integer getPstock() { return stock; }
    public void setPstock(Integer pstock) { this.stock = pstock; }

    public Integer getPcategory() { return categoryId; }
    public void setPcategory(Integer pcategory) { this.categoryId = pcategory; }

    public Integer getPdiscount() { return discount; }
    public void setPdiscount(Integer pdiscount) { this.discount = pdiscount; }

    public MultipartFile getPimage() { return imageFile; }
    public void setPimage(MultipartFile pimage) { this.imageFile = pimage; }

    public MultipartFile getNew_pimage() { return imageFile; }
    public void setNew_pimage(MultipartFile new_pimage) { this.imageFile = new_pimage; }

    public String getOld_pimage() { return image; }
    public void setOld_pimage(String old_pimage) { this.image = old_pimage; }
}
