package jier.plundr.controller;

import jier.plundr.dto.ReturnPageDTO;
import jier.plundr.dto.address.CreateAddressDTO;
import jier.plundr.dto.address.UpdateAddressDTO;
import jier.plundr.model.Address;
import jier.plundr.service.AddressService;
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
public class AddressController {

    @Autowired
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    // ------------------------ Admin Requests -------------------------//

    @GetMapping("/addresses")
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

    @GetMapping("/address/{id}")
    public ResponseEntity<Address> getAddress(@PathVariable("id") long addressId) {
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

    @PostMapping("/address")
    public ResponseEntity<Address> createAddress(@RequestBody CreateAddressDTO createAddressDTO) {
        try {
            Address newAddress = addressService.createAddress(createAddressDTO);

            return new ResponseEntity<>(newAddress, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/address/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable("id") long addressId,
                                                 @RequestBody UpdateAddressDTO updateAddressDTO) {
        try {
            Address address = addressService.updateAddress(addressId, updateAddressDTO);

            return new ResponseEntity<>(address, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable("id") long addressId) {
        try {
            Boolean isDeleted = addressService.deleteAddress(addressId);

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
