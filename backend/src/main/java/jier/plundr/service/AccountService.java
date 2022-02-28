package jier.plundr.service;

import jier.plundr.model.Account;
import jier.plundr.model.Customer;
import jier.plundr.model.enums.AccountType;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    List<Account> findAll(Pageable pageable);

    List<Account> findAllByOwningCustomer(Long customerId);

    Account saveAccount(Account account);

    Account createAccount(AccountType accountType, Long customerId);

    Account updateAccount(Long accountId, AccountType accountType);

    Boolean deleteAccount(Long accountId);

    void deposit(Long accountId, BigDecimal amount);

    void withdraw(Long accountId, BigDecimal amount);

    void transfer(Long senderAccountId, Long recipientAccountId, BigDecimal amount);
}
