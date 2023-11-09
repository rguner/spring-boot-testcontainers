package com.demo.springboottestcontainers;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisConnectionDetails;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
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
    RabbitMQContainer rabbitMqContainer() {
        return new RabbitMQContainer("rabbitmq:3.8.9");
    }

    @Bean
    //@ServiceConnection(name = "redis") //Couldn't get host and port from container with @ServiceConnection annotation for GenericContainer
    GenericContainer redisContainer() {
        GenericContainer redisContainer = new GenericContainer("redis:6.0.5").withExposedPorts(6379);
        redisContainer.start();

        System.setProperty("spring.data.redis.host", redisContainer.getHost());
        System.setProperty("spring.data.redis.port", String.valueOf(redisContainer.getFirstMappedPort()));
        return redisContainer;
    }

    public static void main(String[] args) {
        SpringApplication.from(SpringBootTestcontainersApplication::main)
                .with(TestSpringBootTestcontainersApplication.class)
                .run(args);
    }

}
