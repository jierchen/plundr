package jier.plundr.service;

import jier.plundr.dto.UserDTO;
import jier.plundr.model.Customer;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {

    Customer findByEmail(String email);

    List<Customer> findAll(Pageable pageable);

    Customer saveCustomer(Customer customer);

    Customer createCustomer(UserDTO userDto);

    Customer updateCustomer(Long id, UserDTO userDto);

    Boolean deleteCustomer(Long id);
}
