package com.demo.springboottestcontainers.service;

import com.demo.springboottestcontainers.entity.Customer;
import com.demo.springboottestcontainers.exception.ResourceNotFoundException;
import com.demo.springboottestcontainers.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public void deleteAll() {
        customerRepository.deleteAll();
    }

    public void saveAll(List<Customer> customers) {
        customerRepository.saveAll(customers);
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Customer with id = " + id));

        //return customerRepository.findById(id)
        //        .orElseThrow(() -> new RuntimeException("Not found Customer with id = " + id));
    }
}
