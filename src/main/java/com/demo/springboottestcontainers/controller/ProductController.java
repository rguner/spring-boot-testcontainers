package com.demo.springboottestcontainers.controller;

import com.demo.springboottestcontainers.entity.Customer;
import com.demo.springboottestcontainers.entity.Product;
import com.demo.springboottestcontainers.service.CustomerService;
import com.demo.springboottestcontainers.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @GetMapping("/api/product/{productId}")
  public Product getProductById(@PathVariable("productId") Long productId) {
    return productService.getProductById(productId);
  }

  @PostMapping("/api/saveProductToRedis")
  public Product saveProductToRedis(@RequestBody Product product) {
    return productService.save(product);
  }

}