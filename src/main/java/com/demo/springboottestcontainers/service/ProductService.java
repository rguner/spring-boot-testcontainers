package com.demo.springboottestcontainers.service;

import com.demo.springboottestcontainers.entity.Customer;
import com.demo.springboottestcontainers.entity.Product;
import com.demo.springboottestcontainers.exception.ResourceNotFoundException;
import com.demo.springboottestcontainers.repository.CustomerRepository;
import com.demo.springboottestcontainers.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // to save product as json to redis
    @Qualifier("jsonRedisTemplate")
    private final RedisTemplate jsonRedisTemplate;

    public void deleteAll() {
        productRepository.deleteAll();
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product saveJson(Product product) {
        jsonRedisTemplate.opsForValue().set(product.getId(), product);
        return product;
    }

    public void saveAll(List<Product> products) {
        productRepository.saveAll(products);
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Product with id = " + productId));
    }
}
