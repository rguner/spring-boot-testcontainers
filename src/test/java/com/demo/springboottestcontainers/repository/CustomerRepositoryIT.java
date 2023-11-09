package com.demo.springboottestcontainers.repository;

import com.demo.springboottestcontainers.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
// do not replace the testcontainer data source
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class CustomerRepositoryIT {

    @Autowired
    private CustomerRepository customerRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @Test
    public void testCustomerSaveAndFindById() {

        Customer customer = Customer.builder().name("Ramazan Guner1").email("rguner1@gmail.com").build();
        Customer savedCustomer = customerRepository.save(customer);

        Optional<Customer> result = customerRepository.findById(savedCustomer.getId());
        assertTrue(result.isPresent());

        Customer customerFromGet = result.get();

        assertEquals("Ramazan Guner1", customerFromGet.getName());
        assertEquals("rguner1@gmail.com", customerFromGet.getEmail());

    }

    @Test
    public void testCustomerCrud() {

        Customer customer = Customer.builder().name("Ramazan Guner1").email("rguner1@gmail.com").build();
        Customer savedCustomer = customerRepository.save(customer);

        // read
        Optional<Customer> result = customerRepository.findById(savedCustomer.getId());
        assertTrue(result.isPresent());
        Customer customerFromGet = result.get();
        assertEquals("Ramazan Guner1", customerFromGet.getName());
        assertEquals("rguner1@gmail.com", customerFromGet.getEmail());

        // update
        savedCustomer.setName("Ramazan Guner2");
        savedCustomer.setEmail("rguner2@gmail.com");
        customerRepository.save(savedCustomer);

        // read by name
        Optional<Customer> resultByName = customerRepository.findByName(savedCustomer.getName());
        assertTrue(resultByName.isPresent());
        Customer customerFromResultByName = result.get();
        assertEquals("Ramazan Guner2", customerFromResultByName.getName());
        assertEquals("rguner2@gmail.com", customerFromResultByName.getEmail());

        // delete
        customerRepository.delete(savedCustomer);
        assertTrue(customerRepository.findById(savedCustomer.getId()).isEmpty());

    }

}