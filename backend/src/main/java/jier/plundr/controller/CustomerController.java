package jier.plundr.controller;

import jier.plundr.dto.ReturnPageDTO;
import jier.plundr.dto.customer.AddContactDTO;
import jier.plundr.dto.customer.ContactDTO;
import jier.plundr.dto.customer.CreateCustomerDTO;
import jier.plundr.dto.customer.UpdateCustomerDTO;
import jier.plundr.model.Customer;
import jier.plundr.security.UserDetailsImpl;
import jier.plundr.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // ------------------------ Admin Requests -------------------------//

    @GetMapping("/admin/customers")
    public ResponseEntity<ReturnPageDTO<Customer>> getCustomers(@RequestParam int page, @RequestParam int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            ReturnPageDTO<Customer> customersReturnPage = customerService.findAll(pageable);

            return new ResponseEntity<>(customersReturnPage, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin/customer/{id}")
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

    @PostMapping("/admin/customer")
    public ResponseEntity<Customer> createCustomer(@RequestBody CreateCustomerDTO createCustomerDto) {
        try {
            Customer newCustomer = customerService.createCustomer(createCustomerDto);

            return new ResponseEntity<>(newCustomer, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/customer/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") long customerId,
                                                   @RequestBody UpdateCustomerDTO updateCustomerDto) {
        try {
            Customer customer = customerService.updateCustomer(customerId, updateCustomerDto);

            return new ResponseEntity<>(customer, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/customer/{id}")
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

    @GetMapping("/profile")
    public ResponseEntity<Customer> getCurrentCustomer(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Optional<Customer> optionalCustomer = customerService.findById(userDetails.getId());

            if(optionalCustomer.isPresent()) {
                return new ResponseEntity<>(optionalCustomer.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<Customer> updateCurrentCustomer(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                          @RequestBody UpdateCustomerDTO updateCustomerDto) {
        try {
            Customer customer = customerService.updateCustomer(userDetails.getId(), updateCustomerDto);

            return new ResponseEntity<>(customer, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/contacts")
    public ResponseEntity<ReturnPageDTO<ContactDTO>> getCustomerContacts(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                         @RequestParam int page, @RequestParam int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            ReturnPageDTO<ContactDTO> contactsReturnPage = customerService.getCustomerContacts(userDetails.getId(), pageable);

            return new ResponseEntity<>(contactsReturnPage, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/contact")
    public ResponseEntity<Void> addCustomerContact(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @RequestBody AddContactDTO addContactDto) {
        try {
            customerService.addContactByEmail(userDetails.getId(), addContactDto.getContactEmail());

            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
