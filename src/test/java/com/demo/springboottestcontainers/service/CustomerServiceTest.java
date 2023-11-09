package com.demo.springboottestcontainers.service;

import com.demo.springboottestcontainers.entity.Customer;
import com.demo.springboottestcontainers.exception.ResourceNotFoundException;
import com.demo.springboottestcontainers.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    public void getAll_WhenRepositoryReturnsRecords() {
        Customer customer1 = Customer.builder().name("Ramazan Guner1").email("rguner1@gmail.com").build();
        Customer customer2 = Customer.builder().name("Ramazan Guner2").email("rguner2@gmail.com").build();

        List<Customer> customerList = Arrays.asList(customer1, customer2);
        when(customerRepository.findAll()).thenReturn(customerList);

        List<Customer> customerListResult = customerService.getAll();
        assertEquals(2, customerListResult.size());
        assertEquals("Ramazan Guner1", customerListResult.get(0).getName());
        assertEquals("rguner1@gmail.com", customerListResult.get(0).getEmail());
        assertEquals("Ramazan Guner2", customerListResult.get(1).getName());
        assertEquals("rguner2@gmail.com", customerListResult.get(1).getEmail());
    }

    @Test
    public void getCustomerById_WhenRepositoryReturnsRecord() {
        Customer customer1 = Customer.builder().id(200L).name("Ramazan Guner1").email("rguner1@gmail.com").build();
        when(customerRepository.findById(any())).thenReturn(Optional.of(customer1));

        Customer customer = customerService.getCustomerById(200L);
        assertEquals("Ramazan Guner1", customer.getName());
        assertEquals("rguner1@gmail.com", customer.getEmail());

    }

    @Test
    public void getCustomerById_WhenRepositoryReturnsOptionalNull() {
        when(customerRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> customerService.getCustomerById(200L));
        assertEquals(exception.getClass(), ResourceNotFoundException.class);
        assertTrue(exception.getMessage().contains("Not found Customer with id"));
    }

}
