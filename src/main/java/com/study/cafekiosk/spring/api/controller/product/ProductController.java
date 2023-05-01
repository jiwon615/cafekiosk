package com.study.cafekiosk.spring.api.controller.product;

import com.study.cafekiosk.spring.api.service.product.ProductService;
import com.study.cafekiosk.spring.api.service.product.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/api/v1/products/selling")
    public ResponseEntity<List<ProductResponse>> getSellingProducts() {
        return ResponseEntity.ok().body(productService.getSellingProducts());
    }
}