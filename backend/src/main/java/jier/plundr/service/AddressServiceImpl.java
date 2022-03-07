package jier.plundr.service;

import jier.plundr.dto.AddressDTO;
import jier.plundr.model.Address;
import jier.plundr.model.User;
import jier.plundr.repository.AddressRepository;
import jier.plundr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Address createAddress(AddressDTO addressDto, Long userId) {
        User owningUser = userRepository.getById(userId);

        Address newAddress = new Address();
        newAddress.setStreetAddress(addressDto.getStreetAddress());
        newAddress.setCity(addressDto.getCity());
        newAddress.setProvince(addressDto.getProvince());
        newAddress.setZipCode(addressDto.getZipCode());
        newAddress.setOwningUser(owningUser);

        return this.saveAddress(newAddress);
    }

    @Override
    public Address updateAddress(Long addressId, AddressDTO addressDto) {
        Address address =  addressRepository.getById(addressId);
        address.setStreetAddress(addressDto.getStreetAddress());
        address.setCity(addressDto.getCity());
        address.setProvince(addressDto.getProvince());
        address.setZipCode(addressDto.getZipCode());

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
