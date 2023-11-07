package com.demo.springboottestcontainers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestSpringBootTestcontainersApplication {


    /*
        if there are these beans, SpringApplication connects to these created containers
        else connects to the resources defined in application.yaml
    */
    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    }

    @Bean
    @ServiceConnection
    static RabbitMQContainer rabbitMqContainer() {
        return new RabbitMQContainer("rabbitmq:3.8.9");
    }

    @Bean
    @ServiceConnection(name = "redis")
    static GenericContainer redisContainer() {
        return new GenericContainer ("redis:6.0.5");
    }

    public static void main(String[] args) {
        SpringApplication.from(SpringBootTestcontainersApplication::main)
                .with(TestSpringBootTestcontainersApplication.class)
                .run(args);
    }

}
