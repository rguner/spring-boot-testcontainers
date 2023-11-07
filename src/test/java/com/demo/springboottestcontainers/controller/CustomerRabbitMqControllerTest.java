package com.demo.springboottestcontainers.controller;

import com.demo.springboottestcontainers.entity.Customer;
import com.demo.springboottestcontainers.service.CustomerService;
import com.demo.springboottestcontainers.service.RabbitMqCustomerSender;
import com.rabbitmq.client.AMQP;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@Testcontainers
class CustomerRabbitMqControllerTest {

  @LocalServerPort
  private Integer port;

  @Container
  @ServiceConnection
  static RabbitMQContainer rabbitMqContainer = new RabbitMQContainer("rabbitmq:3.8.9");

  /*
  needed if we want to get message count from queue
  @Autowired
  private RabbitTemplate rabbitTemplate;
   */

  @BeforeEach
  void setUp() {
    RestAssured.baseURI = "http://localhost:" + port;
  }

  @Test
  void sendCustomerToRabbitMq() {
    Customer customer =  new Customer(null, "John", "john@mail.com");

    Response response = given()
      .contentType(ContentType.JSON)
            .and()
            .body(customer)
            .when()
            .post("/api/sendCustomerToRabbitMq")
            .then()
            .extract().response();

    Assertions.assertEquals(200, response.statusCode());
    Assertions.assertEquals(null, response.jsonPath().getString("id"));
    Assertions.assertEquals("John", response.jsonPath().getString("name"));
    Assertions.assertEquals("john@mail.com", response.jsonPath().getString("email"));

    /*
    AMQP.Queue.DeclareOk declareOk = this.rabbitTemplate.execute(channel ->
            channel.queueDeclare("customer-queue", true, false, false,null));
    Assertions.assertEquals(1, declareOk.getMessageCount());
     */
  }

  @Test
  void sendCustomerToRabbitMq_WhenRabbitMqIsDown() {
    Customer customer =  new Customer(null, "John", "john@mail.com");

    rabbitMqContainer.stop();
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