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

    Optional<Address> findById(Long addressId);

    Address saveAddress(Address address);

    Address createAddress(Long userId, CreateAddressDTO createAddressDto);

    Address updateAddress(Long userId, UpdateAddressDTO updateAddressDto);

    Boolean deleteAddress(Long userId);

    Address findAddressByUserId(Long userId);
}
