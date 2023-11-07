package com.demo.springboottestcontainers.service;

import com.demo.springboottestcontainers.entity.Customer;
import com.demo.springboottestcontainers.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }
}
