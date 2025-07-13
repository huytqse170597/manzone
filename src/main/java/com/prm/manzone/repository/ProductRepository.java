package com.prm.manzone.repository;

import com.prm.manzone.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByIdAndDeletedFalse(Integer id);
}
