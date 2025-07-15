package com.prm.manzone.repository;

import com.prm.manzone.entities.User;
import com.prm.manzone.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Page<User> findAll(Specification<User> spec, Pageable pageable);

    // Statistics queries
    @Query("SELECT COUNT(u) FROM User u WHERE u.deleted = false")
    Long countActiveUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.deleted = true")
    Long countDeletedUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.active = true AND u.deleted = false")
    Long countActivatedUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.active = false AND u.deleted = false")
    Long countDeactivatedUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.deleted = false")
    Long countByRole(@Param("role") Role role);

    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :startDate AND u.createdAt < :endDate")
    Long countRegistrationsBetween(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT DATE(u.createdAt), COUNT(u) FROM User u " +
            "WHERE u.createdAt >= :startDate AND u.createdAt < :endDate " +
            "GROUP BY DATE(u.createdAt) " +
            "ORDER BY DATE(u.createdAt)")
    List<Object[]> findDailyRegistrations(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);
}
