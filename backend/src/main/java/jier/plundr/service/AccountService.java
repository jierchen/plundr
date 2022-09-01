package jier.plundr.service;

import jier.plundr.dto.ReturnPageDTO;
import jier.plundr.dto.account.*;
import jier.plundr.model.Account;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    ReturnPageDTO<Account> findAll(Pageable pageable);

    List<AccountDTO> findAllByOwningCustomer(Long customerId);

    Optional<Account> findById(Long accountId);

    Account saveAccount(Account account);

    Account createAccount(Long customerId, CreateAccountDTO createAccountDto);

    Account updateAccount(Long customerId, Long accountId, UpdateAccountDTO updateAccountDto);

    Boolean deleteAccount(Long customerId, Long accountId);

    void setDepositAccount(Long customerId, Long accountId);

    void deposit(Long customerId, Long accountId, DepositDTO depositDto);

    void withdraw(Long customerId, Long accountId, WithdrawDTO withdrawDto);

    void internalTransfer(Long customerId, Long accountId, InternalTransferDTO internalTransferDto);

    void externalTransfer(Long customerId, Long accountId, ExternalTransferDTO externalTransferDto);
}
