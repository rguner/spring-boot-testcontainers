package com.demo.springboottestcontainers.service;

import com.demo.springboottestcontainers.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMqCustomerSender {

    private final RabbitTemplate rabbitTemplate;

    @Value("${testcontainers-app.topic-exchange.name:customer-topic-exchange}")
    private String topicExchange;

    @Value("${testcontainers-app.routing.key.customer:customer-routing-key}")
    private String routingKeyCustomer;

    public void messageSend(Customer customer) {
        rabbitTemplate.convertAndSend(topicExchange, routingKeyCustomer, customer);
    }
}
