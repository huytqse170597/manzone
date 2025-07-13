package com.prm.manzone.service;

import com.prm.manzone.payload.product.ProductResponse;
import org.springframework.data.domain.Page;

public interface IProductService {

    Page<ProductResponse> getAllProducts(Integer page, Integer size, String search);
}
