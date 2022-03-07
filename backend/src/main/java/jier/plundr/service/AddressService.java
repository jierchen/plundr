package jier.plundr.service;

import jier.plundr.dto.AddressDTO;
import jier.plundr.model.Address;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AddressService {

    List<Address> findAll(Pageable pageable);

    Address saveAddress(Address address);

    Address createAddress(AddressDTO addressDto, Long userId);

    Address updateAddress(Long addressId, AddressDTO addressDto);

    Boolean deleteAddress(Long addressId);
}
