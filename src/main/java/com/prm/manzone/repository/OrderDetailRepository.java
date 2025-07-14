package com.prm.manzone.repository;

import com.prm.manzone.entities.Order;
import com.prm.manzone.entities.OrderDetail;
import com.prm.manzone.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    Optional<OrderDetail> findByIdAndDeletedFalse(Integer id);

    List<OrderDetail> findByOrderAndDeletedFalse(Order order);

    List<OrderDetail> findByProductAndDeletedFalse(Product product);

    @Query("SELECT od FROM OrderDetail od WHERE od.order = :order AND od.deleted = false")
    List<OrderDetail> findByOrderOrderById(@Param("order") Order order);

    @Query("SELECT SUM(od.quantity) FROM OrderDetail od WHERE od.product = :product AND od.deleted = false")
    Long getTotalQuantityByProduct(@Param("product") Product product);
}
