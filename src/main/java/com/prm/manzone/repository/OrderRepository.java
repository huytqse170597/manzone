package com.prm.manzone.repository;

import com.prm.manzone.entities.Order;
import com.prm.manzone.entities.User;
import com.prm.manzone.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {
    Optional<Order> findByIdAndDeletedFalse(Integer id);

    Page<Order> findByUserAndDeletedFalse(User user, Pageable pageable);

    Page<Order> findByDeletedFalse(Pageable pageable);

    Page<Order> findByStatusAndDeletedFalse(OrderStatus status, Pageable pageable);

    List<Order> findByUserAndStatusAndDeletedFalse(User user, OrderStatus status);

    List<Order> findByStatusAndDeletedFalse(OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.user = :user AND o.deleted = false ORDER BY o.createdAt DESC")
    List<Order> findByUserOrderByCreatedAtDesc(@Param("user") User user);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.user = :user AND o.status = :status AND o.deleted = false")
    long countByUserAndStatus(@Param("user") User user, @Param("status") OrderStatus status);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status AND o.deleted = false")
    long countByStatus(@Param("status") OrderStatus status);
}
