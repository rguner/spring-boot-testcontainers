package com.demo.springboottestcontainers.controller;

import com.demo.springboottestcontainers.entity.Product;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@Testcontainers
class ProductControllerIT {

  @LocalServerPort
  private Integer port;


  @Container
  @ServiceConnection(name = "redis")
  static GenericContainer redisContainer = new GenericContainer("redis:6.0.5")
          .withExposedPorts(6379);


  @DynamicPropertySource //  //Couldn't get host and port from container with @ServiceConnection annotation for GenericContainer
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.redis.host", redisContainer::getHost);
    registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort);
  }

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
          "postgres:15-alpine"
  );

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
  void getProductById() {

    Response response = given()
            .contentType(ContentType.JSON)
            .when()
            .get("/api/product/1234")
            .then()
            .extract().response();

    Assertions.assertEquals(200, response.statusCode());
    Assertions.assertEquals(1234L, response.jsonPath().getLong("id"));
    Assertions.assertEquals("Product 1", response.jsonPath().getString("name"));
    Assertions.assertEquals("Product1 Description", response.jsonPath().getString("description"));
    Assertions.assertEquals(100, response.jsonPath().getInt("price"));

  }

}