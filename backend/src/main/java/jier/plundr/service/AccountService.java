package jier.plundr.service;

import jier.plundr.dto.ReturnPageDTO;
import jier.plundr.dto.account.*;
import jier.plundr.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {

    ReturnPageDTO<Account> findAll(Pageable pageable);

    List<Account> findAllByOwningCustomer(Long customerId);

    Optional<Account> findById(Long accountId);

    Account saveAccount(Account account);

    Account createAccount(CreateAccountDTO createAccountDto);

    Account updateAccount(Long accountId, UpdateAccountDTO updateAccountDto);

    Boolean deleteAccount(Long accountId);

    void setDepositAccount(Long customerId, Long accountId);

    void deposit(DepositDTO depositDto);

    void withdraw(WithdrawDTO withdrawDto);

    void transfer(TransferDTO transferDto);
}
