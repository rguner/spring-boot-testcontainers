package com.demo.springboottestcontainers.controller;

import java.util.List;

import com.demo.springboottestcontainers.entity.Customer;
import com.demo.springboottestcontainers.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService customerService;

  @GetMapping("/api/customers")
  public List<Customer> getAll() {
    return customerService.getAll();
  }

  @GetMapping("/api/customer/{id}")
  public Customer getCustomerById(@PathVariable("id") Long id) {
    return customerService.getCustomerById(id);
  }

}