package com.prm.manzone.service.impl;

import com.prm.manzone.entities.Category;
import com.prm.manzone.exception.CategoryNotFoundException;
import com.prm.manzone.mapper.CategoryMapper;
import com.prm.manzone.payload.category.CategoryRequest;
import com.prm.manzone.payload.category.CategoryResponse;
import com.prm.manzone.repository.CategoryRepository;
import com.prm.manzone.service.ICategoryService;
import com.prm.manzone.specification.CategorySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        Category foundCategory = categoryRepository.findByNameAndDeletedFalse(request.getName());
        if (foundCategory != null) {
            throw new IllegalArgumentException("Category with name '" + request.getName() + "' already exists.");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        categoryRepository.save(category);
        return CategoryMapper.mapToResponse(category);
    }

    @Override
    public CategoryResponse updateCategory(int id, CategoryRequest request) {
        Category category = categoryRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        categoryRepository.save(category);
        return CategoryMapper.mapToResponse(category);
    }

    @Override
    public void deleteCategory(int id) {
        Category category = categoryRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        category.setDeleted(true);
        categoryRepository.save(category);
    }

    @Override
    public CategoryResponse getCategoryById(int id) {
        Category category = categoryRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return CategoryMapper.mapToResponse(category);
    }

    @Override
    public Page<CategoryResponse> getAllCategories(Integer page, Integer size, String search) {
        Specification<Category> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        Pageable pageable = Pageable.ofSize(size != null ? size : 10).withPage(page != null ? page : 0);
        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("deleted")));

        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and(CategorySpecification.searchByKeyword(search));
        }

        return categoryRepository.findAll(spec, pageable).map(CategoryMapper::mapToResponse);
    }
}
