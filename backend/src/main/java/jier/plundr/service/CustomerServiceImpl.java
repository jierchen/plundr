package jier.plundr.service;

import jier.plundr.dto.ReturnPageDTO;
import jier.plundr.dto.customer.ContactDTO;
import jier.plundr.dto.customer.CreateCustomerDTO;
import jier.plundr.dto.customer.UpdateCustomerDTO;
import jier.plundr.mapper.CustomerMapper;
import jier.plundr.mapper.PageMapper;
import jier.plundr.model.Customer;
import jier.plundr.model.enums.UserType;
import jier.plundr.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PageMapper pageMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Finds all {@code Customers}.
     *
     * @param pageable {@code Pageable} object containing pagination information to limit search results.
     * @return Page information of found {@code Customers}.
     */
    @Override
    public ReturnPageDTO<Customer> findAll(Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        return pageMapper.pageToReturnPageDTO(customerPage);
    }

    /**
     * Finds an {@code Customer} by {@code id}.
     *
     * @param customerId {@code id} of {@code Customer} to find.
     * @return An {@code Optional} object containing the found {@code Customer}
     *         or {@code null} if {@code Customer} is not found.
     */
    @Override
    public Optional<Customer> findById(Long customerId) {
        return customerRepository.findById(customerId);
    }

    /**
     * Finds an {@code Customer} by {@code email}
     *
     * @param email {@code email} of {@code Customer} to find
     * @return The found {@code Customer} or {@code null} if a {@code Customer} is not found
     */
    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    /**
     * Saves a {@code Customer} to keep changes made.
     *
     * @param customer {@code Customer} to save.
     * @return {@code Customer} saved.
     */
    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    /**
     * Creates and saves a new {@code Customer}
     *
     * @param createCustomerDto {@code CreateCustomerDTO} containing information for {@code Customer} creation
     * @return {@code Customer} created and saved
     */
    @Override
    public Customer createCustomer(CreateCustomerDTO createCustomerDto) {
        Customer newCustomer = new Customer();

        // User fields
        newCustomer.setFirstName(createCustomerDto.getFirstName());
        newCustomer.setLastName(createCustomerDto.getLastName());
        newCustomer.setPhoneNumber(createCustomerDto.getPhoneNumber());
        newCustomer.setDateOfBirth(createCustomerDto.getDateOfBirth());
        newCustomer.setEmail(createCustomerDto.getEmail());
        newCustomer.setUsername(createCustomerDto.getUsername());
        newCustomer.setPassword(passwordEncoder.encode(createCustomerDto.getPassword()));
        newCustomer.setType(UserType.CUSTOMER);

        return this.saveCustomer(newCustomer);
    }

    /**
     * Updates and saves an existing {@code Customer}.
     *
     * @param customerId @{code id} of {@code Customer} to update.
     * @param updateCustomerDto  {@code UpdateCustomerDTO} containing information for {@code Customer} updating.
     * @return {@code Customer} updated and saved.
     */
    @Override
    public Customer updateCustomer(Long customerId, UpdateCustomerDTO updateCustomerDto) {
        Customer customer = customerRepository.getById(customerId);

        if(updateCustomerDto.getFirstName() != null)
            customer.setFirstName(updateCustomerDto.getFirstName());
        if(updateCustomerDto.getLastName() != null)
            customer.setLastName(updateCustomerDto.getLastName());
        if(updateCustomerDto.getPhoneNumber() != null)
            customer.setPhoneNumber(updateCustomerDto.getPhoneNumber());
        if(updateCustomerDto.getDateOfBirth() != null)
            customer.setDateOfBirth(updateCustomerDto.getDateOfBirth());
        if(updateCustomerDto.getEmail() != null)
            customer.setEmail(updateCustomerDto.getEmail());
        if(updateCustomerDto.getUsername() != null)
            customer.setUsername(updateCustomerDto.getUsername());
        if(updateCustomerDto.getPassword() != null)
            customer.setPassword(passwordEncoder.encode(updateCustomerDto.getPassword()));

        return this.saveCustomer(customer);
    }

    /**
     * Deletes an existing {@code Customer}.
     *
     * @param customerId {@code id} of {@code Customer} to delete.
     * @return Return {@code True} if found {@code Customer} has been successfully deleted, {@code False} otherwise.
     */
    @Override
    public Boolean deleteCustomer(Long customerId) {
        if(customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
            return true;
        }
        return false;
    }

    /**
     * Retrieve {@code Customer} contacts from a {@code Customer}.
     *
     * @param customerId {@code id} of {@code Customer} to retrieve contacts.
     * @param pageable {@code Pageable} object containing pagination information to limit search results.
     * @return Page information of found {@code Customers} contacts.
     */
    @Override
    public ReturnPageDTO<ContactDTO> getCustomerContacts(Long customerId, Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findContactsByCustomerId(customerId, pageable);
        ReturnPageDTO<Customer> customerReturnPageDTO = pageMapper.pageToReturnPageDTO(customerPage);
        return customerMapper.customerPageDTOToContactPageDTO(customerReturnPageDTO);
    }

    /**
     * Adds a {@code Customer} contact by {@code email}.
     *
     * @param customerId {@code id} of {@code Customer}.
     * @param contactEmail {@code email} of {@code Customer} contact.
     */
    @Override
    public void addContactByEmail(Long customerId, String contactEmail) {
        Customer customer = customerRepository.getById(customerId);
        Customer contact = customerRepository.findByEmail(contactEmail);

        if (customerId != contact.getId() &&!customer.getContacts().contains(contact) && customer.getContacts().size() < 100) {
            customer.getContacts().add(contact);
            this.saveCustomer(customer);
        }
    }

    @Override
    public Boolean deleteContactById(Long customerId, Long contactId) {
        try {
            customerRepository.deleteContactByContactId(customerId, contactId);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
