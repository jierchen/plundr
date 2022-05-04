package jier.plundr.validation;

import jier.plundr.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountValidator {

    public Boolean isCustomerAccount(Long customerId, Account account) {
        if (account.getOwningCustomer().getId() == customerId) {
            return true;
        }

        return false;
    }
}
