package jier.plundr.service;

import jier.plundr.dto.customer.CreateCustomerDTO;
import jier.plundr.dto.customer.UpdateCustomerDTO;
import jier.plundr.model.Customer;
import jier.plundr.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> findAll(Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        return customerPage.getContent();
    }

    @Override
    public Optional<Customer> findById(Long customerId) {
        return customerRepository.findById(customerId);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer createCustomer(CreateCustomerDTO createCustomerDto) {
        Customer newCustomer = new Customer();

        newCustomer.setFirstName(createCustomerDto.getFirstName());
        newCustomer.setLastName(createCustomerDto.getLastName());
        newCustomer.setPhoneNumber(createCustomerDto.getPhoneNumber());
        newCustomer.setDateOfBirth(createCustomerDto.getDateOfBirth());
        newCustomer.setEmail(createCustomerDto.getEmail());
        newCustomer.setUsername(createCustomerDto.getUsername());
        newCustomer.setPassword(createCustomerDto.getPassword());

        return this.saveCustomer(newCustomer);
    }

    @Override
    public Customer updateCustomer(Long customerId, UpdateCustomerDTO updateCustomerDto) {
        Customer customer = customerRepository.getById(customerId);

        if(updateCustomerDto.getFirstName() == null)
            customer.setFirstName(updateCustomerDto.getFirstName());
        if(updateCustomerDto.getLastName() == null)
            customer.setLastName(updateCustomerDto.getLastName());
        if(updateCustomerDto.getPhoneNumber() == null)
            customer.setPhoneNumber(updateCustomerDto.getPhoneNumber());
        if(updateCustomerDto.getDateOfBirth() == null)
            customer.setDateOfBirth(updateCustomerDto.getDateOfBirth());
        if(updateCustomerDto.getEmail() == null)
            customer.setEmail(updateCustomerDto.getEmail());
        if(updateCustomerDto.getUsername() == null)
            customer.setUsername(updateCustomerDto.getUsername());
        if(updateCustomerDto.getPassword() == null)
            customer.setPassword(updateCustomerDto.getPassword());

        return this.saveCustomer(customer);
    }

    @Override
    public Boolean deleteCustomer(Long customerId) {
        if(customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
            return true;
        }
        return false;
    }
}
