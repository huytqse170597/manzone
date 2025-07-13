package com.prm.manzone.service.impl;

import com.prm.manzone.entities.Product;
import com.prm.manzone.mapper.ProductMapper;
import com.prm.manzone.payload.product.ProductResponse;
import com.prm.manzone.repository.ProductRepository;
import com.prm.manzone.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

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
}
