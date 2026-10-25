package org.example.wedbanhoa.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private Integer categoryId;
    private String categoryName;
    private Long productCount;

    // Aliases for template compatibility (${cat.cid}, ${cat.cname})
    public Integer getCid() { return categoryId; }
    public String getCname() { return categoryName; }
}
