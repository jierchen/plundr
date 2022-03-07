package jier.plundr.service;

import jier.plundr.dto.UserDTO;
import jier.plundr.model.Customer;
import jier.plundr.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer createCustomer(UserDTO userDto) {
        Customer newCustomer = new Customer();

        newCustomer.setFirstName(userDto.getFirstName());
        newCustomer.setLastName(userDto.getLastName());
        newCustomer.setPhoneNumber(userDto.getPhoneNumber());
        newCustomer.setDateOfBirth(userDto.getDateOfBirth());
        newCustomer.setEmail(userDto.getEmail());
        newCustomer.setUsername(userDto.getUsername());
        newCustomer.setPassword(userDto.getPassword());

        return this.saveCustomer(newCustomer);
    }

    @Override
    public Customer updateCustomer(Long customerId, UserDTO userDto) {
        Customer customer = customerRepository.getById(customerId);

        customer.setFirstName(userDto.getFirstName());
        customer.setLastName(userDto.getLastName());
        customer.setPhoneNumber(userDto.getPhoneNumber());
        customer.setDateOfBirth(userDto.getDateOfBirth());
        customer.setEmail(userDto.getEmail());
        customer.setUsername(userDto.getUsername());
        customer.setPassword(userDto.getPassword());

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
