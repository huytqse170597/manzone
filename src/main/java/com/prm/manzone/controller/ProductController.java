package com.prm.manzone.controller;

import com.prm.manzone.payload.ApiResponse;
import com.prm.manzone.payload.category.CategoryResponse;
import com.prm.manzone.payload.product.ProductRequest;
import com.prm.manzone.payload.product.ProductResponse;
import com.prm.manzone.service.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "API for managing products")
public class ProductController {

    private final IProductService productService;

    @Operation(summary = "Get all Products")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProducts(
            @RequestParam(required = false) Integer page,
            @RequestParam (required = false) Integer size,
            @RequestParam(required = false) String search) {
        Page<ProductResponse> response = productService.getAllProducts(page, size, search);
        return ResponseEntity.ok(
                ApiResponse.<Page<ProductResponse>>builder()
                        .success(true)
                        .message("Products retrieved successfully")
                        .data(response)
                        .build());
    }

    @Operation(summary = "Delete a product")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Product deleted successfully")
                        .build());
    }

    @Operation(summary = "Create a new product")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@RequestBody @Valid ProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<ProductResponse>builder()
                        .success(true)
                        .message("Product created successfully")
                        .data(response)
                        .build());
    }

    @Operation(summary = "Update an existing product")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable int id, @RequestBody @Valid ProductRequest request) {
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(
                ApiResponse.<ProductResponse>builder()
                        .success(true)
                        .message("Product updated successfully")
                        .data(response)
                        .build());
    }

    @Operation(summary = "Get a product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable int id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(
                ApiResponse.<ProductResponse>builder()
                        .success(true)
                        .message("Product retrieved successfully")
                        .data(response)
                        .build());
    }
}
