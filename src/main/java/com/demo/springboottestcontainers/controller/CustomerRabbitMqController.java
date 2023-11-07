package com.demo.springboottestcontainers.controller;

import com.demo.springboottestcontainers.entity.Customer;
import com.demo.springboottestcontainers.service.RabbitMqCustomerSender;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomerRabbitMqController {

  private final RabbitMqCustomerSender rabbitMqCustomerSender;


  @PostMapping("/api/sendCustomerToRabbitMq")
  public Customer sendCustomerToRabbitMq(@RequestBody Customer customer) {
    rabbitMqCustomerSender.messageSend(customer);
    return customer;
  }
  
}