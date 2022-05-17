package jier.plundr.service;

import jier.plundr.dto.ReturnPageDTO;
import jier.plundr.dto.account.*;
import jier.plundr.mapper.PageMapper;
import jier.plundr.model.Account;
import jier.plundr.model.Customer;
import jier.plundr.model.Transaction;
import jier.plundr.model.enums.AccountType;
import jier.plundr.model.enums.TransactionType;
import jier.plundr.repository.AccountRepository;
import jier.plundr.repository.CustomerRepository;
import jier.plundr.repository.TransactionRepository;
import jier.plundr.validation.AccountValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Setter
    @Getter
    @AllArgsConstructor
    public static class AccountTypeData {
        private BigDecimal withdrawFee;
        private BigDecimal interestRate;
    }

    public static final Map<AccountType, AccountTypeData> ACCOUNT_TYPE_MAP = Map.of(
            AccountType.CHEQUING, new AccountTypeData(new BigDecimal("0.00"), new BigDecimal("0.00")),
            AccountType.SAVINGS, new AccountTypeData(new BigDecimal("5.00"), new BigDecimal("0.025"))
    );

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PageMapper pageMapper;

    @Autowired
    private AccountValidator accountValidator;

    /**
     * Finds all {@code Accounts}.
     *
     * @param pageable {@code Pageable} object containing pagination information to limit search results.
     * @return Page information of found {@code Accounts}.
     */
    @Override
    public ReturnPageDTO<Account> findAll(Pageable pageable) {
        Page<Account> accountPage = accountRepository.findAll(pageable);
        return pageMapper.pageToReturnPageDTO(accountPage);
    }

    /**
     * Finds all {@code Accounts} belonging to a {@code Customer}.
     *
     * @param customerId {@code id} of {@code Customer} owning the {@code Accounts}.
     * @return List of {@code Accounts} belonging to found {@code Customer}.
     */
    @Override
    public List<Account> findAllByOwningCustomer(Long customerId) {
        Customer owningCustomer = customerRepository.getById(customerId);
        return accountRepository.findAllByOwningCustomer(owningCustomer);
    }

    /**
     * Finds an {@code Account} by {@code id}.
     *
     * @param accountId {@code id} of {@code Account} to find.
     * @return An {@code Optional} object containing the found {@code Account}
     *         or {@code null} if {@code Account} is not found.
     */
    @Override
    public Optional<Account> findById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    /**
     * Saves an {@code Account} to keep changes made.
     *
     * @param account {@code Account} to save.
     * @return {@code Account} saved by the {@code AccountRepository}.
     */
    @Override
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    /**
     * Creates and saves a new {@code Account}.
     *
     * @param customerId {@code id} of {@code Customer} owning the new {@code Account}.
     * @param createAccountDto  {@code CreateAccountDTO} containing information for {@code Account} creation.
     * @return {@code Account} created and saved.
     */
    @Override
    public Account createAccount(Long customerId, CreateAccountDTO createAccountDto) {
        Customer owningCustomer = customerRepository.getById(customerId);

        Account newAccount = new Account();
        newAccount.setAccountType(createAccountDto.getAccountType());
        newAccount.setName(createAccountDto.getName());
        newAccount.setOwningCustomer(owningCustomer);
        newAccount.setBalance(new BigDecimal("0.00"));

        AccountTypeData data = ACCOUNT_TYPE_MAP.get(createAccountDto.getAccountType());

        if(data == null) {
            return null;
        } else {
            newAccount.setWithdrawFee(data.getWithdrawFee());
            newAccount.setInterestRate(data.getInterestRate());
        }

        return this.saveAccount(newAccount);
    }

    /**
     * Updates and saves an existing {@code Account}.
     *
     * @param customerId {@code id} of belonging {@code Customer}
     * @param accountId {@code id} of {@code Account} to update.
     * @param updateAccountDto  {@code CreateAccountDTO} containing information for {@code Account} updating.
     * @return {@code Account} updated and saved.
     */
    @Override
    public Account updateAccount(Long customerId, Long accountId, UpdateAccountDTO updateAccountDto) {
        Account account = accountRepository.getById(accountId);

        if (accountValidator.isCustomerAccount(customerId, account)) {
            if(updateAccountDto.getName() != null) {
                account.setName(updateAccountDto.getName());
            }

            AccountTypeData data = ACCOUNT_TYPE_MAP.get(updateAccountDto.getAccountType());

            if (data != null) {
                account.setAccountType(updateAccountDto.getAccountType());
                account.setWithdrawFee(data.getWithdrawFee());
                account.setInterestRate(data.getInterestRate());
            }

            return accountRepository.save(account);
        }

        return account;
    }

    /**
     * Deletes an existing {@code Account}.
     *
     * @param customerId {@code id} of belonging {@code Customer}
     * @param accountId {@code id} of {@code Account} to delete.
     * @return Return {@code True} if found {@code Account} has been successfully deleted, {@code False} otherwise.
     */
    @Override
    public Boolean deleteAccount(Long customerId, Long accountId) {
        Account account = accountRepository.getById(accountId);

        if(accountValidator.isCustomerAccount(customerId, account)){
            accountRepository.deleteById(accountId);
            return true;
        }
        return false;
    }

    /**
     * Sets the {@code depositAccount} for a {@code Customer}.
     *
     * @param customerId {@code id} of belonging {@code Customer}
     * @param accountId {@code id} of Account to set as {@code depositAccount}.
     */
    @Override
    public void setDepositAccount(Long customerId, Long accountId) {
        Account account = accountRepository.getById(accountId);

        if(accountValidator.isCustomerAccount(customerId, account)){
            Customer customer = customerRepository.getById(customerId);
            customer.setDepositAccount(account);
            customerRepository.save(customer);
        }
    }

    /**
     * Makes a deposit for an {@code Account}.
     *
     * @param customerId {@code id} of belonging {@code Customer}
     * @param accountId {@code id} of {@code Account} for deposit.
     * @param depositDto {@code DepositDTO} containing information for a deposit.
     */
    @Override
    public void deposit(Long customerId, Long accountId, DepositDTO depositDto) {
        Account account = accountRepository.getById(accountId);

        if(accountValidator.isCustomerAccount(customerId, account)) {
            BigDecimal amount = depositDto.getAmount();

            assert amount.compareTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)) > 0;

            account.setBalance(account.getBalance().add(amount));

            Transaction depositTransaction = new Transaction();
            depositTransaction.setTransactionType(TransactionType.DEPOSIT);
            depositTransaction.setAmount(amount);
            depositTransaction.setDescription(depositDto.getDescription());
            depositTransaction.setOwningAccount(account);

            transactionRepository.save(depositTransaction);
            accountRepository.save(account);
        }
    }

    /**
     * Makes a deposit for an {@code Account}.
     *
     * @param customerId {@code id} of belonging {@code Customer}
     * @param accountId {@code id} of {@code Account} for withdraw.
     * @param withdrawDto {@code WithdrawDTO} containing information for a withdrawal.
     */
    @Override
    public void withdraw(Long customerId, Long accountId, WithdrawDTO withdrawDto) {
        Account account = accountRepository.getById(accountId);

        if(accountValidator.isCustomerAccount(customerId, account)) {
            BigDecimal amount = withdrawDto.getAmount();
            assert amount.compareTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)) > 0;

            assert account.getBalance().compareTo(amount) > 0;
            account.setBalance(account.getBalance().subtract(amount));

            Transaction withdrawTransaction = new Transaction();
            withdrawTransaction.setTransactionType(TransactionType.WITHDRAW);
            withdrawTransaction.setAmount(amount);
            withdrawTransaction.setDescription(withdrawDto.getDescription());
            withdrawTransaction.setOwningAccount(account);

            transactionRepository.save(withdrawTransaction);
            accountRepository.save(account);
        }
    }

    /**
     * Makes an internal transfer to an owned {@code Account}.
     *
     * @param customerId {@code id} of {@code Customer}
     * @param accountId {@code id} of sender {@code Account} for transfer.
     * @param internalTransferDto {@code InternalTransferDTO} containing information for the internal transfer.
     */
    @Override
    public void internalTransfer(Long customerId, Long accountId, InternalTransferDTO internalTransferDto) {
        Account senderAccount = accountRepository.getById(accountId);

        if(accountValidator.isCustomerAccount(customerId, senderAccount)) {
            BigDecimal amount = internalTransferDto.getAmount();
            assert amount.compareTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)) > 0;

            Account recipientAccount = accountRepository.getById(internalTransferDto.getRecipientAccountId());

            if(accountValidator.isCustomerAccount(customerId, recipientAccount)
                    && senderAccount.getId() != recipientAccount.getId()) {
                assert senderAccount.getBalance().compareTo(amount) > 0;
                senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
                recipientAccount.setBalance(recipientAccount.getBalance().add(amount));

                Transaction transferTransaction = new Transaction();
                transferTransaction.setOwningAccount(senderAccount);
                transferTransaction.setRecipientAccount(recipientAccount);
                transferTransaction.setAmount(amount);
                transferTransaction.setTransactionType(TransactionType.INT_TRANSFER);
                transferTransaction.setDescription(internalTransferDto.getDescription());

                transactionRepository.save(transferTransaction);
                accountRepository.save(senderAccount);
                accountRepository.save(recipientAccount);
            }
        }
    }

    /**
     * Makes an external transfer to a contact's deposit {@code Account}.
     *
     * @param customerId {@code id} of {@code Customer}
     * @param accountId {@code id} of sender {@code Account} for transfer.
     * @param externalTransferDto {@code InternalTransferDTO} containing information for the external transfer.
     */
    @Override
    public void externalTransfer(Long customerId, Long accountId, ExternalTransferDTO externalTransferDto) {
        Account senderAccount = accountRepository.getById(accountId);

        if(accountValidator.isCustomerAccount(customerId, senderAccount)) {
            BigDecimal amount = externalTransferDto.getAmount();
            assert amount.compareTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)) > 0;

            Customer recipient = customerRepository.getById(externalTransferDto.getRecipientId());
            Account recipientDepositAccount = recipient.getDepositAccount();

            assert senderAccount.getBalance().compareTo(amount) > 0;
            senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
            recipientDepositAccount.setBalance(recipientDepositAccount.getBalance().add(amount));

            Transaction transferTransaction = new Transaction();
            transferTransaction.setOwningAccount(senderAccount);
            transferTransaction.setRecipientAccount(recipientDepositAccount);
            transferTransaction.setAmount(amount);
            transferTransaction.setTransactionType(TransactionType.EXT_TRANSFER);
            transferTransaction.setDescription(externalTransferDto.getDescription());

            transactionRepository.save(transferTransaction);
            accountRepository.save(senderAccount);
            accountRepository.save(recipientDepositAccount);
        }
    }
}
