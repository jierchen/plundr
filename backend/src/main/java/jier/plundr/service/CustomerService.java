package jier.plundr.service;

import jier.plundr.dto.ReturnPageDTO;
import jier.plundr.dto.customer.ContactDTO;
import jier.plundr.dto.customer.CreateCustomerDTO;
import jier.plundr.dto.customer.UpdateCustomerDTO;
import jier.plundr.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    ReturnPageDTO<Customer> findAll(Pageable pageable);

    Optional<Customer> findById(Long customerId);

    Customer findByEmail(String email);

    Customer saveCustomer(Customer customer);

    Customer createCustomer(CreateCustomerDTO createCustomerDto);

    Customer updateCustomer(Long customerId, UpdateCustomerDTO updateCustomerDto);

    Boolean deleteCustomer(Long customerId);

    ReturnPageDTO<ContactDTO> getCustomerContacts(Long customerId, Pageable pageable);

    void addContactByEmail(Long customerId, String contactEmail);

    Boolean deleteContactById(Long customerId, Long contactId);
}
