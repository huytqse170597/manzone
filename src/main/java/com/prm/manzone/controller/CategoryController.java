package com.prm.manzone.controller;

import com.prm.manzone.payload.ApiResponse;
import com.prm.manzone.payload.category.CategoryRequest;
import com.prm.manzone.payload.category.CategoryResponse;
import com.prm.manzone.service.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Category", description = "API for managing categories")
public class CategoryController {

    private final ICategoryService categoryService;

    @Operation(summary = "Create a new category")
    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@RequestBody @Valid CategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<CategoryResponse>builder()
                        .success(true)
                        .message("Category created successfully")
                        .data(response)
                        .build());
    }

    @Operation(summary = "Update an existing category")
    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable int id,
            @RequestBody @Valid CategoryRequest request) {
        CategoryResponse response = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(
                ApiResponse.<CategoryResponse>builder()
                        .success(true)
                        .message("Category updated successfully")
                        .data(response)
                        .build());
    }

    @Operation(summary = "Delete a category")
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Category deleted successfully")
                        .build());
    }

    @Operation(summary = "Get a category by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable int id) {
        CategoryResponse response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(
                ApiResponse.<CategoryResponse>builder()
                        .success(true)
                        .message("Category retrieved successfully")
                        .data(response)
                        .build());
    }

    @Operation(summary = "Get all categories")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<CategoryResponse>>> getAllCategories(
            @RequestParam (required = false) Integer page,
            @RequestParam (required = false) Integer size,
            @RequestParam(required = false) String search) {
        Page<CategoryResponse> response = categoryService.getAllCategories(page, size, search);
        return ResponseEntity.ok(
                ApiResponse.<Page<CategoryResponse>>builder()
                        .success(true)
                        .message("Categories retrieved successfully")
                        .data(response)
                        .build());
    }
}
