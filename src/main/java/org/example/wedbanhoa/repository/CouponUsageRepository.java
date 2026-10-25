package org.example.wedbanhoa.repository;

import org.example.wedbanhoa.entity.CouponUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CouponUsageRepository extends JpaRepository<CouponUsage, Integer> {
    List<CouponUsage> findByCouponId(Integer couponId);
    List<CouponUsage> findByUserId(Integer userId);
}