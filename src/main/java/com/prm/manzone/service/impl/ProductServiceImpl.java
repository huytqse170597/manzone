package com.prm.manzone.service.impl;

import com.prm.manzone.entities.Category;
import com.prm.manzone.entities.Product;
import com.prm.manzone.mapper.ProductMapper;
import com.prm.manzone.payload.product.ProductRequest;
import com.prm.manzone.payload.product.ProductResponse;
import com.prm.manzone.repository.CategoryRepository;
import com.prm.manzone.repository.ProductRepository;
import com.prm.manzone.service.IProductService;
import com.prm.manzone.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<ProductResponse> getAllProducts(Integer page, Integer size, String search) {
        Specification<Product> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        Pageable pageable = Pageable.ofSize(size != null ? size : 10).withPage(page != null ? page : 0);
        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("deleted")));

        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and(ProductSpecification.searchByKeyword(search));
        }

        return productRepository.findAll(spec, pageable).map(ProductMapper::mapToResponse);
    }

    @Override
    public void deleteProduct(int id) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setDeleted(true);
        productRepository.save(product);
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findByIdAndDeletedFalse(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrls(request.getImageUrls());
        product.setCategory(category);
        Product saved = productRepository.save(product);
        return ProductMapper.mapToResponse(saved);
    }

    @Override
    public ProductResponse updateProduct(int id, ProductRequest request) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Category category = categoryRepository.findByIdAndDeletedFalse(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrls(request.getImageUrls());
        product.setCategory(category);
        Product saved = productRepository.save(product);
        return ProductMapper.mapToResponse(saved);
    }

    @Override
    public ProductResponse getProductById(int id) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return ProductMapper.mapToResponse(product);
    }
}
