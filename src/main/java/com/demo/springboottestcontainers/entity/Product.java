package com.demo.springboottestcontainers.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("Product")
@Data
@Builder
public class Product {

    private Long id;
    private String name;
    private String description;
    private int price;
}
