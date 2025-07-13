package com.prm.manzone.service;

import com.prm.manzone.payload.category.CategoryRequest;
import com.prm.manzone.payload.category.CategoryResponse;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse updateCategory(int id, CategoryRequest request);

    void deleteCategory(int id);

    CategoryResponse getCategoryById(int id);

    Page<CategoryResponse> getAllCategories(Integer page, Integer size, String search);
}
