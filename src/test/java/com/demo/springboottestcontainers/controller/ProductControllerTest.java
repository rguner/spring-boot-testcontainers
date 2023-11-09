package com.demo.springboottestcontainers.controller;

import com.demo.springboottestcontainers.entity.Customer;
import com.demo.springboottestcontainers.entity.Product;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@Testcontainers
class ProductControllerTest {

  @LocalServerPort
  private Integer port;

  @Container
  @ServiceConnection(name = "redis")
  static GenericContainer redisContainer = new GenericContainer("redis:6.0.5");

  @BeforeEach
  void setUp() {
    RestAssured.baseURI = "http://localhost:" + port;
  }

  @Test
  void saveProductToRedis() {
    Product product = Product.builder()
            .id(1234L)
            .name("Product 1")
            .description("Product1 Description")
            .price(100)
            .build();

    Response response = given()
      .contentType(ContentType.JSON)
            .and()
            .body(product)
            .when()
            .post("/api/saveProductToRedis")
            .then()
            .extract().response();

    Assertions.assertEquals(200, response.statusCode());
    Assertions.assertEquals(1234L, response.jsonPath().getLong("id"));
    Assertions.assertEquals("Product 1", response.jsonPath().getString("name"));
    Assertions.assertEquals("Product1 Description", response.jsonPath().getString("description"));
    Assertions.assertEquals(100, response.jsonPath().getInt("price"));

  }

  @Test
  void sendCustomerToRabbitMq_WhenRabbitMqIsDown() {
    Customer customer =  new Customer(null, "John", "john@mail.com");

    redisContainer.stop();
    Response response = given()
            .contentType(ContentType.JSON)
            .and()
            .body(customer)
            .when()
            .post("/api/sendCustomerToRabbitMq")
            .then()
            .extract().response();

    Assertions.assertEquals(500, response.statusCode());

  }

}