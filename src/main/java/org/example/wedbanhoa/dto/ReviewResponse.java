package org.example.wedbanhoa.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private Integer reviewId;
    private Integer userId;
    private String fullname;
    private String email;
    private Integer productId;
    private String productName;
    private Integer rating;
    private String comment;
    private LocalDateTime reviewDate;

    public String getReview_date() {
        if (reviewDate == null) return "";
        return reviewDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getProduct_name() {
        return productName != null ? productName : "";
    }
}
