package jier.plundr.controller;

import jier.plundr.dto.customer.AddContactDTO;
import jier.plundr.dto.customer.CreateCustomerDTO;
import jier.plundr.dto.customer.UpdateCustomerDTO;
import jier.plundr.model.Customer;
import jier.plundr.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // ------------------------ Admin Requests -------------------------//

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getCustomers(@RequestParam int page, @RequestParam int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            List<Customer> customers = customerService.findAll(pageable);

            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") long customerId) {
        try {
            Optional<Customer> optionalCustomer = customerService.findById(customerId);

            if(optionalCustomer.isPresent()) {
                return new ResponseEntity<>(optionalCustomer.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/customer")
    public ResponseEntity<Customer> createCustomer(@RequestBody CreateCustomerDTO createCustomerDto) {
        try {
            Customer newCustomer = customerService.createCustomer(createCustomerDto);

            return new ResponseEntity<>(newCustomer, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/customer/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") long customerId,
                                                   @RequestBody UpdateCustomerDTO updateCustomerDto) {
        try {
            Customer customer = customerService.updateCustomer(customerId, updateCustomerDto);

            return new ResponseEntity<>(customer, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/customer/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") long customerId) {
        try {
            Boolean isDeleted = customerService.deleteCustomer(customerId);

            if(isDeleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // ------------------------ Customer Requests -------------------------//

    @GetMapping("/customer/{id}/contacts")
    public ResponseEntity<List<Customer>> getCustomerContacts(@PathVariable("id") long customerId,
                                                              @RequestParam int page, @RequestParam int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            List<Customer> contacts = customerService.getCustomerContacts(customerId, pageable);

            return new ResponseEntity<>(contacts, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/customer/{id}/contacts")
    public ResponseEntity<Void> addCustomerContact(@PathVariable("id") long customerId,
                                                   @RequestBody AddContactDTO addContactDto) {
        try {
            customerService.addContactByEmail(customerId, addContactDto.getContactEmail());

            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
