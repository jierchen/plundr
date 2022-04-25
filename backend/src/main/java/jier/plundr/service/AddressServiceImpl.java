package jier.plundr.service;

import jier.plundr.dto.ReturnPageDTO;
import jier.plundr.dto.address.CreateAddressDTO;
import jier.plundr.dto.address.UpdateAddressDTO;
import jier.plundr.mapper.PageMapper;
import jier.plundr.model.Address;
import jier.plundr.model.User;
import jier.plundr.repository.AddressRepository;
import jier.plundr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PageMapper pageMapper;

    /**
     * Finds all {@code Addresses}.
     *
     * @param pageable {@code Pageable} object containing pagination information to limit search results.
     * @return Page information of found {@code Addresses}.
     */
    @Override
    public ReturnPageDTO<Address> findAll(Pageable pageable) {
        Page<Address> addressPage = addressRepository.findAll(pageable);
        return pageMapper.pageToReturnPageDTO(addressPage);
    }

    /**
     * Finds an {@code Address} by {@code id}.
     *
     * @param addressId {@code id} of {@code Address} to find.
     * @return An {@code Optional} object containing the found {@code Address}
     *         or {@code null} if {@code Address} is not found.
     */
    @Override
    public Optional<Address> findById(Long addressId) {
        return addressRepository.findById(addressId);
    }

    /**
     * Saves an {@code Address} to keep changes made.
     *
     * @param address {@code Address} to save.
     * @return {@code Address} saved.
     */
    @Override
    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    /**
     * Creates and saves a new {@code Address}
     *
     * @param createAddressDto {@code CreateAddressDTO} containing information for {@code Address} creation
     * @return {@code Address} created and saved
     */
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

    /**
     * Updates and saves an existing {@code Address}.
     *
     * @param addressId @{code id} of {@code Address} to update.
     * @param updateAddressDto  {@code UpdateAddressDTO} containing information for {@code Address} updating.
     * @return {@code Address} updated and saved.
     */
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

    /**
     * Deletes an existing {@code Address}.
     *
     * @param addressId {@code id} of {@code Address} to delete.
     * @return Return {@code True} if found {@code Address} has been successfully deleted, {@code False} otherwise.
     */
    @Override
    public Boolean deleteAddress(Long addressId) {
        if(addressRepository.existsById(addressId)){
            addressRepository.deleteById(addressId);
            return true;
        }
        return false;
    }
}
