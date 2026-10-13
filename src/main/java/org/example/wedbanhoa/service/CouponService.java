package org.example.wedbanhoa.service;

import org.example.wedbanhoa.dto.CouponRequest;
import org.example.wedbanhoa.dto.CouponResponse;
import org.example.wedbanhoa.dto.CouponUsageResponse;
import org.example.wedbanhoa.entity.Coupon;
import org.example.wedbanhoa.entity.CouponUsage;
import org.example.wedbanhoa.repository.AccountRepository;
import org.example.wedbanhoa.repository.CouponRepository;
import org.example.wedbanhoa.repository.CouponUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponUsageRepository couponUsageRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public List<CouponResponse> getAllCoupons() {
        return couponRepository.findAll().stream()
                .map(this::toResponse) // Gọi hàm mapping nội bộ
                .toList();
    }

    @Transactional(readOnly = true)
    public CouponResponse getById(Integer id) {
        return couponRepository.findById(id)
                .map(this::toResponse) // Gọi hàm mapping nội bộ
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public CouponResponse getByCode(String code) {
        return couponRepository.findByCode(code)
                .map(this::toResponse) // Gọi hàm mapping nội bộ
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public Coupon getEntityById(Integer id) {
        return couponRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Coupon getEntityByCode(String code) {
        return couponRepository.findByCode(code).orElse(null);
    }

    @Transactional
    public boolean insertCoupon(CouponRequest request) {
        try {
            Coupon c = toEntity(request); // Gọi hàm mapping nội bộ
            couponRepository.save(c);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean insertCoupon(String code, Integer discountPercent, LocalDate expiryDate, Integer status, Double minOrderValue) {
        CouponRequest req = CouponRequest.builder()
                .code(code)
                .discountPercent(discountPercent)
                .expiryDate(expiryDate)
                .status(status)
                .minOrderValue(minOrderValue)
                .build();
        return insertCoupon(req);
    }

    @Transactional
    public boolean updateCoupon(CouponRequest request) {
        Coupon c = couponRepository.findById(request.getCouponId()).orElse(null);
        if (c != null) {
            updateEntityFromRequest(c, request); // Gọi hàm cập nhật nội bộ
            couponRepository.save(c);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateCoupon(Integer id, String code, Integer discountPercent, LocalDate expiryDate, Integer status, Double minOrderValue) {
        CouponRequest req = CouponRequest.builder()
                .couponId(id)
                .code(code)
                .discountPercent(discountPercent)
                .expiryDate(expiryDate)
                .status(status)
                .minOrderValue(minOrderValue)
                .build();
        return updateCoupon(req);
    }

    @Transactional
    public boolean deleteCoupon(Integer id) {
        try {
            couponRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean disableCoupon(String code) {
        Optional<Coupon> opt = couponRepository.findByCode(code);
        if (opt.isPresent()) {
            Coupon c = opt.get();
            c.setStatus(0);
            couponRepository.save(c);
            return true;
        }
        return false;
    }

    @Transactional
    public String generateCoupon() {
        String randomCode = "VIP-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        Coupon c = Coupon.builder()
                .code(randomCode)
                .discountPercent(10)
                .expiryDate(LocalDate.now().plusMonths(1))
                .status(1)
                .minOrderValue(1.0)
                .build();
        couponRepository.save(c);
        return randomCode;
    }

    @Transactional(readOnly = true)
    public List<CouponUsageResponse> getCouponUsageHistory(Integer couponId) {
        return couponUsageRepository.findByCouponId(couponId).stream()
                .map(u -> {
                    String userEmail = accountRepository.findById(u.getUserId())
                            .map(a -> a.getEmail())
                            .orElse("");
                    String couponCode = couponRepository.findById(u.getCouponId())
                            .map(c -> c.getCode())
                            .orElse("");
                    return CouponUsageResponse.builder()
                            .usageId(u.getUsageId())
                            .couponId(u.getCouponId())
                            .userId(u.getUserId())
                            .orderId(u.getOrderId())
                            .usedAt(u.getUsedAt())
                            .userEmail(userEmail)
                            .couponCode(couponCode)
                            .build();
                })
                .toList();
    }

    @Transactional
    public void recordUsage(Integer couponId, Integer userId, Integer orderId) {
        CouponUsage usage = CouponUsage.builder()
                .couponId(couponId)
                .userId(userId)
                .orderId(orderId)
                .usedAt(LocalDateTime.now())
                .build();
        couponUsageRepository.save(usage);
    }

    // =========================================================================
    // CÁC HÀM MAPPING ĐƯỢC ĐẶT Ở ĐÂY
    // =========================================================================

    private Coupon toEntity(CouponRequest request) {
        if (request == null) return null;
        return Coupon.builder()
                .couponId(request.getCouponId())
                .code(request.getCode())
                .discountPercent(request.getDiscountPercent())
                .expiryDate(request.getExpiryDate())
                .status(request.getStatus() != null ? request.getStatus() : 1)
                .minOrderValue(request.getMinOrderValue() != null ? request.getMinOrderValue() : 0.0)
                .build();
    }

    private void updateEntityFromRequest(Coupon coupon, CouponRequest request) {
        if (coupon == null || request == null) return;
        coupon.setCode(request.getCode());
        coupon.setDiscountPercent(request.getDiscountPercent());
        coupon.setExpiryDate(request.getExpiryDate());
        coupon.setStatus(request.getStatus());
        coupon.setMinOrderValue(request.getMinOrderValue());
    }

    private CouponResponse toResponse(Coupon entity) {
        if (entity == null) return null;
        return CouponResponse.builder()
                .couponId(entity.getCouponId())
                .code(entity.getCode())
                .discountPercent(entity.getDiscountPercent())
                .expiryDate(entity.getExpiryDate())
                .status(entity.getStatus())
                .minOrderValue(entity.getMinOrderValue())
                .build();
    }

    private CouponRequest toRequest(Coupon entity) {
        if (entity == null) return null;
        return CouponRequest.builder()
                .couponId(entity.getCouponId())
                .code(entity.getCode())
                .discountPercent(entity.getDiscountPercent())
                .expiryDate(entity.getExpiryDate())
                .status(entity.getStatus())
                .minOrderValue(entity.getMinOrderValue())
                .build();
    }
}