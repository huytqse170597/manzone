package com.prm.manzone.repository;

import com.prm.manzone.entities.Category;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {

    Optional<Category> findByIdAndDeletedFalse(Integer id);

    Category findByNameAndDeletedFalse(@NotBlank(message = "Tên danh mục là bắt buộc") String name);
}
