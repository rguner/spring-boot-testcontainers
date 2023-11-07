package com.demo.springboottestcontainers.service;

import com.demo.springboottestcontainers.entity.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMqCustomerListener {

    @RabbitListener(queues = {"${testcontainers-app.queue.name.customer:customer-queue}"})
    public void receiveMessage(Customer customer) {
        log.info("Customer: Received <{} {}> , thread: {}", customer, Thread.currentThread().getName());
    }

}
