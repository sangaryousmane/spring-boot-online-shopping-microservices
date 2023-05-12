package com.example.productmicroservice.service;

import com.example.productmicroservice.model.ProductRequest;
import com.example.productmicroservice.model.ProductResponse;

import java.util.List;

public interface ProductService {

    Long addProduct(ProductRequest product);

    ProductResponse getProductById(Long productId);

    void reduceQuantity(Long productId, Long quantity);

    List<ProductResponse> getProducts();
    ProductRequest updateProduct(ProductRequest product, Long id);
}
