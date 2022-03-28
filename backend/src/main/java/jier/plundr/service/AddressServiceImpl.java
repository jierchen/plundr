package jier.plundr.service;

import jier.plundr.dto.address.CreateAddressDTO;
import jier.plundr.dto.address.UpdateAddressDTO;
import jier.plundr.model.Address;
import jier.plundr.model.User;
import jier.plundr.repository.AddressRepository;
import jier.plundr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private final AddressRepository addressRepository;

    @Autowired
    private final UserRepository userRepository;

    public AddressServiceImpl(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Address> findAll(Pageable pageable) {
        Page<Address> addressPage = addressRepository.findAll(pageable);
        return addressPage.getContent();
    }

    @Override
    public Optional<Address> findAddressById(Long id) {
        return addressRepository.findById(id);
    }

    @Override
    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Address createAddress(CreateAddressDTO createAddressDto) {
        User owningUser = userRepository.getById(createAddressDto.getUserId());

        Address newAddress = new Address();
        newAddress.setStreetAddress(createAddressDto.getStreetAddress());
        newAddress.setCity(createAddressDto.getCity());
        newAddress.setProvince(createAddressDto.getProvince());
        newAddress.setZipCode(createAddressDto.getZipCode());
        newAddress.setOwningUser(owningUser);

        return this.saveAddress(newAddress);
    }

    @Override
    public Address updateAddress(Long addressId, UpdateAddressDTO updateAddressDto) {
        Address address =  addressRepository.getById(addressId);

        if(updateAddressDto.getStreetAddress() != null)
            address.setStreetAddress(updateAddressDto.getStreetAddress());
        if(updateAddressDto.getCity() != null)
            address.setCity(updateAddressDto.getCity());
        if(updateAddressDto.getProvince() != null)
            address.setProvince(updateAddressDto.getProvince());
        if(updateAddressDto.getZipCode() != null)
            address.setZipCode(updateAddressDto.getZipCode());

        return this.saveAddress(address);
    }

    @Override
    public Boolean deleteAddress(Long addressId) {
        if(addressRepository.existsById(addressId)){
            addressRepository.deleteById(addressId);
            return true;
        }
        return false;
    }
}
