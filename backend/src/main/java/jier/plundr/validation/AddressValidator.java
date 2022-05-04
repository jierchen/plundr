package jier.plundr.validation;

import jier.plundr.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressValidator {

    public Boolean isUserAddress(Long userId, Address address) {
        if (address.getOwningUser().getId() == userId) {
            return true;
        }

        return false;
    }
}
