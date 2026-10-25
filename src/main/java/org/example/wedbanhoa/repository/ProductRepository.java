package org.example.wedbanhoa.repository;

import org.example.wedbanhoa.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByDiscountGreaterThan(Integer minDiscount);

    List<Product> findByProductIdIn(Collection<Integer> ids);

    @Query("SELECT p FROM Product p WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:categoryId IS NULL OR p.categoryId = :categoryId)")
    List<Product> searchProducts(@Param("keyword") String keyword, @Param("categoryId") Integer categoryId);

    @Query("SELECT p FROM Product p WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:categoryId IS NULL OR p.categoryId = :categoryId)")
    Page<Product> searchProductsPageable(@Param("keyword") String keyword, @Param("categoryId") Integer categoryId, Pageable pageable);

    Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);

    long countByCategoryId(Integer categoryId);
















}