package org.example.wedbanhoa.repository;

import org.example.wedbanhoa.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByOrderId(Integer orderId);

    @Query("SELECT COALESCE(SUM(od.quantity), 0) FROM OrderDetail od JOIN Order o ON od.orderId = o.orderId WHERE od.productId = :productId AND o.status = 'Completed'")
    Integer getSoldQuantityByProductId(@Param("productId") Integer productId);
}
