package org.example.wedbanhoa.repository;

import org.example.wedbanhoa.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status != 'Unpaid'")
    long countValidOrders();

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0.0) FROM Order o WHERE o.status = 'Completed'")
    Double getTotalRevenue();

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0.0) FROM Order o WHERE o.userId = :userId AND o.status = 'Completed'")
    Double getTotalSpentByUser(@Param("userId") Integer userId);

    List<Order> findByUserIdAndStatusNotOrderByOrderDateDesc(Integer userId, String status);

    Page<Order> findByStatusNotOrderByOrderDateDesc(String status, Pageable pageable);

    List<Order> findByStatusNotOrderByOrderDateDesc(String status);
}
