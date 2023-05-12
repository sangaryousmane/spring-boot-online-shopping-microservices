package com.example.productmicroservice.service;


import com.example.productmicroservice.entities.Product;
import com.example.productmicroservice.exceptions.ProductNotFoundException;
import com.example.productmicroservice.model.ProductRequest;
import com.example.productmicroservice.model.ProductResponse;
import com.example.productmicroservice.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@Log4j2
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Long addProduct(ProductRequest product) {
        log.info("Adding product");
        Product productEntity = new Product();
        productEntity.setProductName(product.getProductName());
        productEntity.setQuantity(product.getQuantity());
        productEntity.setPrice(product.getPrice());
        productRepository.save(productEntity);

        log.info("Product created!");
        return productEntity.getId();
    }

    @Override
    public ProductResponse getProductById(Long productId) {
        log.info("Getting product by Id " + productId);
        Product productID = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product ID not found"));

        ProductResponse response = new ProductResponse();
        response.setProductId(productID.getId());
        response.setProductName(productID.getProductName());
        response.setQuantity(productID.getQuantity());
        response.setPrice(productID.getPrice());
        return response;
    }

    @Override
    public void reduceQuantity(Long productId, Long quantity) {
        log.info("Reduce quantity {} for Id: {}", quantity, productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException("product with given Id "+ productId +" not found"));

        // If available quantity is less than the needed one, throw an exception
        if (product.getQuantity() < quantity){
            throw new ProductNotFoundException("Product doesn't have sufficient quantity.");
        }

        // if there are more quantity available
        // subtract the needed quantity from the available one and save the updated product
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        log.info("Product Quantity updated successfully.");
    }

    @Override
    public List<ProductResponse> getProducts() {
        return productRepository.findAll().stream().map(product -> {
            ProductResponse response=new ProductResponse();
            BeanUtils.copyProperties(product, response);
            return response;
        }).collect(Collectors.toList());
    }

    @Override
    public ProductRequest updateProduct(ProductRequest product, Long id) {
        Product productEntity=productRepository.findById(id)
                .orElseThrow(()->{
                    throw new ProductNotFoundException("product with id "+ id + " not found");
                });

        log.info("Updating product...");
        productEntity.setProductName(product.getProductName());
        BeanUtils.copyProperties(product, productEntity);
        productRepository.save(productEntity);

        log.info("Product updated!");
        return product;
    }

}
