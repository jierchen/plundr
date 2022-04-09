package jier.plundr.service;

import jier.plundr.dto.ReturnPageDTO;
import jier.plundr.dto.address.CreateAddressDTO;
import jier.plundr.dto.address.UpdateAddressDTO;
import jier.plundr.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AddressService {

    ReturnPageDTO<Address> findAll(Pageable pageable);

    Optional<Address> findAddressById(Long id);

    Address saveAddress(Address address);

    Address createAddress(CreateAddressDTO createAddressDto);

    Address updateAddress(Long addressId, UpdateAddressDTO updateAddressDto);

    Boolean deleteAddress(Long addressId);
}
