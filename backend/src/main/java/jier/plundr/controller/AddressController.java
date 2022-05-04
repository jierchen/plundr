package jier.plundr.controller;

import jier.plundr.dto.ReturnPageDTO;
import jier.plundr.dto.address.CreateAddressDTO;
import jier.plundr.dto.address.UpdateAddressDTO;
import jier.plundr.model.Address;
import jier.plundr.model.User;
import jier.plundr.security.UserDetailsImpl;
import jier.plundr.service.AddressService;
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
public class AddressController {

    @Autowired
    private AddressService addressService;

    // ------------------------ Admin Requests -------------------------//

    @GetMapping("/admin/addresses")
    public ResponseEntity<ReturnPageDTO<Address>> getAllAddresses(@RequestParam int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            ReturnPageDTO<Address> addressesReturnPage = addressService.findAll(pageable);

            return new ResponseEntity<>(addressesReturnPage, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin/address/{addressId}")
    public ResponseEntity<Address> getAddress(@PathVariable("addressId") long addressId) {
        try {
            Optional<Address> optionalAddress = addressService.findById(addressId);

            if(optionalAddress.isPresent()) {
                return new ResponseEntity<>(optionalAddress.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/admin/user/{userId}/address")
    public ResponseEntity<Address> createAddress(@PathVariable("userId") long userId,
                                                 @RequestBody CreateAddressDTO createAddressDTO) {
        try {
            Address newAddress = addressService.createAddress(userId, createAddressDTO);

            return new ResponseEntity<>(newAddress, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/user/{userId}/address")
    public ResponseEntity<Address> updateAddress(@PathVariable("userId") long userId,
                                                 @RequestBody UpdateAddressDTO updateAddressDTO) {
        try {
            Address address = addressService.updateAddress(userId, updateAddressDTO);

            return new ResponseEntity<>(address, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/admin/user/{userId}/address")
    public ResponseEntity<Void> deleteAddress(@PathVariable("userId") long userId) {
        try {
            Boolean isDeleted = addressService.deleteAddress(userId);

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

    @GetMapping("/address")
    public ResponseEntity<User> getUserAddress(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Address address = addressService.findAddressByUserId(userDetails.getId());

            return new ResponseEntity<>(address.getOwningUser(), HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/address")
    public ResponseEntity<Address> createUserAddress(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @RequestBody CreateAddressDTO createAddressDTO) {
        try {
            Address newAddress = addressService.createAddress(userDetails.getId(), createAddressDTO);

            return new ResponseEntity<>(newAddress, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/address")
    public ResponseEntity<Address> updateUserAddress(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @RequestBody UpdateAddressDTO updateAddressDTO) {
        try {
            Address address = addressService.updateAddress(userDetails.getId(), updateAddressDTO);

            return new ResponseEntity<>(address, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/address")
    public ResponseEntity<Void> deleteUserAddress(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Boolean isDeleted = addressService.deleteAddress(userDetails.getId());

            if(isDeleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
