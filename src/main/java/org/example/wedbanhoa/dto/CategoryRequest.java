package org.example.wedbanhoa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequest {

    private Integer categoryId;

    @NotBlank(message = "Tên danh mục không được để trống")
    private String categoryName;

    // Aliases for form parameters cid / cname
    public Integer getCid() { return categoryId; }
    public void setCid(Integer cid) { this.categoryId = cid; }

    public String getCname() { return categoryName; }
    public void setCname(String cname) { this.categoryName = cname; }
}
