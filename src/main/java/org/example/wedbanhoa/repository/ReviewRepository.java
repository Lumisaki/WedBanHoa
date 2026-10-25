package org.example.wedbanhoa.repository;

import org.example.wedbanhoa.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @EntityGraph(attributePaths = {"user", "product"})
    List<Review> findByProductIdOrderByReviewDateDesc(Integer productId);

    @EntityGraph(attributePaths = {"user", "product"})
    List<Review> findByUserIdOrderByReviewDateDesc(Integer userId);

    @EntityGraph(attributePaths = {"user", "product"})
    Page<Review> findAllByOrderByReviewDateDesc(Pageable pageable);

    long countByProductId(Integer productId);
}
