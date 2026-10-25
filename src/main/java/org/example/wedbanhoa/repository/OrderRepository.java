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

    long countByStatus(String status);

    List<Order> findByUserIdAndStatusNotOrderByOrderDateDesc(Integer userId, String status);

    Page<Order> findByStatusNotOrderByOrderDateDesc(String status, Pageable pageable);

    List<Order> findByStatusNotOrderByOrderDateDesc(String status);

    Page<Order> findByStatusOrderByOrderDateDesc(String status, Pageable pageable);

    @Query("SELECT FUNCTION('MONTH', o.orderDate) as m, COALESCE(SUM(o.totalPrice), 0.0) as total " +
           "FROM Order o " +
           "WHERE FUNCTION('YEAR', o.orderDate) = :year AND o.status != 'Unpaid' AND o.status != 'Đã hủy' AND o.status != 'Cancelled' " +
           "GROUP BY FUNCTION('MONTH', o.orderDate) " +
           "ORDER BY FUNCTION('MONTH', o.orderDate)")
    List<Object[]> getMonthlyRevenueByYear(@Param("year") int year);
}
