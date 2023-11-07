package com.demo.springboottestcontainers.repository;

import com.demo.springboottestcontainers.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

}