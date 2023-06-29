package jier.plundr.service;

import jier.plundr.dto.ReturnPageDTO;
import jier.plundr.dto.address.CreateAddressDTO;
import jier.plundr.dto.address.UpdateAddressDTO;
import jier.plundr.mapper.PageMapper;
import jier.plundr.model.Address;
import jier.plundr.model.PlundrUser;
import jier.plundr.repository.AddressRepository;
import jier.plundr.repository.UserRepository;
import jier.plundr.validation.AddressValidator;
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

    @Autowired
    private AddressValidator addressValidator;

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
     * Creates and saves a new {@code Address}.
     *
     * @param userId {@code id} of belonging {@User}
     * @param createAddressDto {@code CreateAddressDTO} containing information for {@code Address} creation
     * @return {@code Address} created and saved
     */
    @Override
    public Address createAddress(Long userId, CreateAddressDTO createAddressDto) {
        PlundrUser owningUser = userRepository.getById(userId);

        Address newAddress = new Address();
        newAddress.setStreetAddress(createAddressDto.getStreetAddress());
        newAddress.setCity(createAddressDto.getCity());
        newAddress.setProvince(createAddressDto.getProvince());
        newAddress.setZipCode(createAddressDto.getZipCode());
        newAddress.setOwningUser(owningUser);

        owningUser.setAddress(this.saveAddress(newAddress));
        userRepository.save(owningUser);

        return newAddress;
    }

    /**
     * Updates and saves an existing {@code Address}.
     *
     * @param userId {@code id} of {@User} associated with the {@code Address}
     * @param updateAddressDto  {@code UpdateAddressDTO} containing information for {@code Address} updating.
     * @return {@code Address} updated and saved.
     */
    @Override
    public Address updateAddress(Long userId, UpdateAddressDTO updateAddressDto) {
        PlundrUser user = userRepository.getById(userId);
        Address address =  user.getAddress();

        if (addressValidator.isUserAddress(userId, address)) {
            if (updateAddressDto.getStreetAddress() != null)
                address.setStreetAddress(updateAddressDto.getStreetAddress());
            if (updateAddressDto.getCity() != null)
                address.setCity(updateAddressDto.getCity());
            if (updateAddressDto.getProvince() != null)
                address.setProvince(updateAddressDto.getProvince());
            if (updateAddressDto.getZipCode() != null)
                address.setZipCode(updateAddressDto.getZipCode());
        }

        return this.saveAddress(address);
    }

    /**
     * Deletes an existing {@code Address}.
     *
     * @param userId {@code id} of {@User} associated with the {@code Address}
     * @return Return {@code True} if found {@code Address} has been successfully deleted, {@code False} otherwise.
     */
    @Override
    public Boolean deleteAddress(Long userId) {
        PlundrUser user = userRepository.getById(userId);
        Address address =  user.getAddress();

        if (addressValidator.isUserAddress(userId, address)) {
            user.setAddress(null);
            addressRepository.deleteById(address.getId());
            userRepository.save(user);
            return true;
        }

        return false;
    }

    /**
     * Retrieves the {@code Address} of its associated {@code User}
     * @param userId userId {@code id} of {@User} associated with the {@code Address}
     * @return {@code Address} of the found {@code User}
     */
    public Address findAddressByUserId(Long userId) {
        PlundrUser user = userRepository.getById(userId);

        return user.getAddress();
    }
}
