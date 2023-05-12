package com.example.productmicroservice.controller;
import com.example.productmicroservice.model.ProductRequest;
import com.example.productmicroservice.model.ProductResponse;
import com.example.productmicroservice.service.ProductServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductServiceImpl productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }


    @PostMapping("/save")
    public ResponseEntity<Long> addProduct(@RequestBody ProductRequest productRequest) {
        Long productId = productService.addProduct(productRequest);
        return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("productId") Long productId) {
        ProductResponse productById = productService.getProductById(productId);
        return new ResponseEntity<>(productById, OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductResponse>> findAllProducts() {
        return new ResponseEntity<>(productService.getProducts(), OK);
    }

    // TODO: API to reduce product quantity
    @PutMapping("/reduceQuantity/{id}")
    public ResponseEntity<Void> reduceQuantity(
            @PathVariable("id") Long productId,
            @RequestParam Long quantity) {
        productService.reduceQuantity(productId, quantity);
        return new ResponseEntity<>(OK);
    }

    // TODO: update a product by name
    @PutMapping("/update/{id}")
    public ResponseEntity<ProductRequest> updateProduct(
            @PathVariable(name = "id") Long id,
            @RequestBody ProductRequest product){
        ProductRequest productRequest = productService.updateProduct(product, id);
        return new ResponseEntity<>(productRequest, OK);
    }
}
