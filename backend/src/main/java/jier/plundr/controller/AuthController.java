package jier.plundr.controller;

import jier.plundr.dto.customer.CreateCustomerDTO;
import jier.plundr.dto.security.LoginRequest;
import jier.plundr.model.Customer;
import jier.plundr.repository.UserRepository;
import jier.plundr.security.UserDetailsImpl;
import jier.plundr.security.UserDetailsServiceImpl;
import jier.plundr.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/customerLogin")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/customerLogout")
    public ResponseEntity<Void> logout() {
        SecurityContextHolder.getContext().setAuthentication(null);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<Customer> signup(@RequestBody CreateCustomerDTO createCustomerDto) {
        try {
            Customer newCustomer = customerService.createCustomer(createCustomerDto);

            return new ResponseEntity<>(newCustomer, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
