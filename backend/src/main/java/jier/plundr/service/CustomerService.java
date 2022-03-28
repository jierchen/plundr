package jier.plundr.service;

import jier.plundr.dto.customer.CreateCustomerDTO;
import jier.plundr.dto.customer.UpdateCustomerDTO;
import jier.plundr.model.Customer;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<Customer> findAll(Pageable pageable);

    Optional<Customer> findById(Long customerId);

    Customer saveCustomer(Customer customer);

    Customer createCustomer(CreateCustomerDTO createCustomerDto);

    Customer updateCustomer(Long customerId, UpdateCustomerDTO updateCustomerDto);

    Boolean deleteCustomer(Long customerId);
}
