package com.demo.springboottestcontainers.controller;

import com.demo.springboottestcontainers.entity.Customer;
import com.demo.springboottestcontainers.service.CustomerService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@Testcontainers
class CustomerControllerIT {

  @LocalServerPort
  private Integer port;

  /**
   * If the container is a static field, it will be initiated once before all the tests and terminated after all
   * the tests. Which means all tests share a single container.
   *
   * If the container is a non-static field, it will be initiated before each test and terminated after each test.
   * Which means each test has its container.
   */
  @Container
  @ServiceConnection // With @ServiceConnection annotation we can remove @DynamicPropertySource annotation.
  static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
          "postgres:15-alpine"
  );

  /*
  With the Testcontainers JUnit 5 Extension,
  we can use @Testcontainers and @Container to automatically start and stop the container.
  @BeforeAll
  static void beforeAll() {
    postgres.start();
  }

  @AfterAll
  static void afterAll() {
    postgres.stop();
  }
  */

  /* With @ServiceConnection annotation we can remove this @DynamicPropertySource annotated method
  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }
   */


  @Autowired
  CustomerService customerService;

  @BeforeEach
  void setUp() {
    RestAssured.baseURI = "http://localhost:" + port;
    customerService.deleteAll();
  }

  @Test
  void logTestContainerDetails() {
    log.info("____________ Test container details ____________");
    log.info("Jdbc Url: {} ", postgresContainer.getJdbcUrl());
    log.info("Username: {} ", postgresContainer.getUsername());
    log.info("Password: {} ", postgresContainer.getPassword());
  }

  @Test
  void shouldGetAllCustomers() {
    List<Customer> customers = List.of(
      new Customer(null, "John", "john@mail.com"),
      new Customer(null, "Dennis", "dennis@mail.com")
    );
    customerService.saveAll(customers);

    given()
      .contentType(ContentType.JSON)
      .when()
      .get("/api/customers")
      .then()
      .statusCode(200)
      .body(".", hasSize(2));
  }

}