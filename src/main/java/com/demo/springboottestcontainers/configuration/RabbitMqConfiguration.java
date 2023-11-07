package com.demo.springboottestcontainers.configuration;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {

    @Value("${testcontainers-app.topic-exchange.name:customer-topic-exchange}")
    private String topicExchange;

    @Value("${testcontainers-app.queue.name.customer:customer-queue}")
    private String queueCustomer;

    @Value("${testcontainers-app.routing.key.customer:customer-routing-key}")
    private String routingKeyCustomer;

    @Bean
    public Queue queueCustomer() {
        return new Queue(queueCustomer);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(topicExchange);
    }


    @Bean
    public Binding bindingCustomer() {
        return BindingBuilder
                .bind(queueCustomer())
                .to(topicExchange())
                .with(routingKeyCustomer);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
