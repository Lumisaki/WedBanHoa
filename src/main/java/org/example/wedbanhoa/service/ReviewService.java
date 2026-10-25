package org.example.wedbanhoa.service;

import org.example.wedbanhoa.dto.ReviewRequest;
import org.example.wedbanhoa.dto.ReviewResponse;
import org.example.wedbanhoa.entity.Account;
import org.example.wedbanhoa.entity.Product;
import org.example.wedbanhoa.entity.Review;
import org.example.wedbanhoa.repository.AccountRepository;
import org.example.wedbanhoa.repository.ProductRepository;
import org.example.wedbanhoa.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByProductId(Integer productId) {
        return reviewRepository.findByProductIdOrderByReviewDateDesc(productId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByUserId(Integer userId) {
        return reviewRepository.findByUserIdOrderByReviewDateDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ReviewResponse saveReview(ReviewRequest request) {
        Review review = Review.builder()
                .userId(request.getUserId())
                .productId(request.getProductId())
                .rating(request.getRating())
                .comment(request.getComment())
                .reviewDate(LocalDateTime.now())
                .build();
        Review saved = reviewRepository.save(review);
        return toResponse(saved);
    }

    @Transactional
    public boolean deleteReview(Integer reviewId) {
        if (reviewId != null && reviewRepository.existsById(reviewId)) {
            reviewRepository.deleteById(reviewId);
            return true;
        }
        return false;
    }

    private ReviewResponse toResponse(Review r) {
        if (r == null) return null;
        String fullname = "Khách hàng";
        String email = "";
        if (r.getUser() != null) {
            fullname = r.getUser().getFullname();
            email = r.getUser().getEmail();
        } else if (r.getUserId() != null) {
            Account acc = accountRepository.findById(r.getUserId()).orElse(null);
            if (acc != null) {
                fullname = acc.getFullname();
                email = acc.getEmail();
            }
        }
        String pName = "";
        if (r.getProduct() != null) {
            pName = r.getProduct().getProductName();
        } else if (r.getProductId() != null) {
            Product p = productRepository.findById(r.getProductId()).orElse(null);
            if (p != null) {
                pName = p.getProductName();
            }
        }
        return ReviewResponse.builder()
                .reviewId(r.getReviewId())
                .userId(r.getUserId())
                .fullname(fullname)
                .email(email)
                .productId(r.getProductId())
                .productName(pName)
                .rating(r.getRating())
                .comment(r.getComment())
                .reviewDate(r.getReviewDate())
                .build();
    }
}
