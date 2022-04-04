package jier.plundr.service;

import jier.plundr.dto.account.*;
import jier.plundr.model.Account;
import jier.plundr.model.Customer;
import jier.plundr.model.Transaction;
import jier.plundr.model.enums.AccountType;
import jier.plundr.model.enums.TransactionType;
import jier.plundr.repository.AccountRepository;
import jier.plundr.repository.CustomerRepository;
import jier.plundr.repository.TransactionRepository;
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
    private final AccountRepository accountRepository;

    @Autowired
    private final CustomerRepository customerRepository;

    @Autowired
    private final TransactionRepository transactionRepository;

    public AccountServiceImpl(AccountRepository accountRepository, CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Finds all {@code Accounts}.
     *
     * @param pageable {@code Pageable} object containing pagination information to limit search results.
     * @return List of found {@code Accounts}.
     */
    @Override
    public List<Account> findAll(Pageable pageable) {
        Page<Account> accountPage = accountRepository.findAll(pageable);
        return accountPage.getContent();
    }

    /**
     * Finds all {@code Accounts} belonging to a {@code Customer}.
     *
     * @param customerId ID of {@code Customer} owning the {@code Accounts}.
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
     * @param createAccountDto  {@code CreateAccountDTO} containing information limited to {@code Account} creation.
     * @return {@code Account} created and saved.
     */
    @Override
    public Account createAccount(CreateAccountDTO createAccountDto) {
        Customer owningCustomer = customerRepository.getById(createAccountDto.getOwningCustomerId());

        Account newAccount = new Account();
        newAccount.setAccountType(createAccountDto.getAccountType());
        newAccount.setOwningCustomer(owningCustomer);
        newAccount.setBalance(new BigDecimal("0.00"));
        // newAccount.setTransactions(new ArrayList<Transaction>());

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
     * @param accountId @{code id} of {@code Account} to update.
     * @param updateAccountDto  {@code CreateAccountDTO} containing information limited to {@code Account} updating.
     * @return {@code Account} updated and saved.
     */
    @Override
    public Account updateAccount(Long accountId, UpdateAccountDTO updateAccountDto) {
        Account account = accountRepository.getById(accountId);

        AccountTypeData data = ACCOUNT_TYPE_MAP.get(updateAccountDto.getAccountType());

        if(data == null) {
            return null;
        } else {
            account.setAccountType(updateAccountDto.getAccountType());
            account.setWithdrawFee(data.getWithdrawFee());
            account.setInterestRate(data.getInterestRate());
        }

        return accountRepository.save(account);
    }

    /**
     * Deletes an existing {@code Account}.
     *
     * @param accountId {@code id} of {@code Account} to delete.
     * @return Return {@code True} if found {@code Account} has been successfully deleted, {@code False} otherwise.
     */
    @Override
    public Boolean deleteAccount(Long accountId) {
        if(accountRepository.existsById(accountId)){
            accountRepository.deleteById(accountId);
            return true;
        }
        return false;
    }

    /**
     * Sets the {@code depositAccount} for a {@code Customer}.
     *
     * @param customerId {@code id} of Customer.
     * @param accountId {@code id} of Account to set as {@code depositAccount}.
     */
    @Override
    public void setDepositAccount(Long customerId, Long accountId) {
        Customer customer = customerRepository.getById(customerId);
        Account account = accountRepository.getById(accountId);

        if(account.getOwningCustomer().getId() == customerId) {
            customer.setDepositAccount(account);
        }
    }

    /**
     * Makes a deposit for an {@code Account}.
     *
     * @param depositDto {@code DepositDTO} containing information for a deposit.
     */
    @Override
    public void deposit(DepositDTO depositDto) {
        BigDecimal amount = depositDto.getAmount();

        assert amount.compareTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)) > 0;

        Account account = accountRepository.getById(depositDto.getAccountId());
        account.setBalance(account.getBalance().add(amount));

        Transaction depositTransaction = new Transaction();
        depositTransaction.setTransactionType(TransactionType.DEPOSIT);
        depositTransaction.setAmount(amount);
        depositTransaction.setDescription(String.format("Deposit: %s", depositDto.getDescription()));
        depositTransaction.setOwningAccount(account);

        transactionRepository.save(depositTransaction);
        accountRepository.save(account);
    }

    /**
     * Makes a deposit for an {@code Account}.
     *
     * @param withdrawDto {@code WithdrawDTO} containing information for a withdraw.
     */
    @Override
    public void withdraw(WithdrawDTO withdrawDto) {
        BigDecimal amount = withdrawDto.getAmount();
        assert amount.compareTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)) > 0;

        Account account = accountRepository.getById(withdrawDto.getAccountId());
        account.setBalance(account.getBalance().subtract(amount));

        Transaction withdrawTransaction = new Transaction();
        withdrawTransaction.setTransactionType(TransactionType.WITHDRAW);
        withdrawTransaction.setAmount(amount);
        withdrawTransaction.setDescription(String.format("Withdraw: %s", withdrawDto.getDescription()));
        withdrawTransaction.setOwningAccount(account);

        transactionRepository.save(withdrawTransaction);
        accountRepository.save(account);
    }

    /**
     * Makes a transfer for an {@code Account}.
     *
     * @param transferDto {@code TransferDTO} containing information for a transfer.
     */
    @Override
    public void transfer(TransferDTO transferDto) {
        BigDecimal amount = transferDto.getAmount();

        assert amount.compareTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)) > 0;

        Account senderAccount = accountRepository.getById(transferDto.getSenderAccountId());
        Account recipientAccount = accountRepository.getById(transferDto.getRecipientAccountId());
        assert senderAccount.getBalance().compareTo(amount) > 0;
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        recipientAccount.setBalance(recipientAccount.getBalance().add(amount));

        Transaction transferTransaction = new Transaction();
        transferTransaction.setOwningAccount(senderAccount);
        transferTransaction.setRecipientAccount(recipientAccount);
        transferTransaction.setAmount(amount);

        if(senderAccount.getOwningCustomer().getId().equals(recipientAccount.getOwningCustomer().getId())) {
            transferTransaction.setTransactionType(TransactionType.INT_TRANSFER);
            transferTransaction.setDescription(
                    String.format("Internal transfer: %s", transferDto.getDescription()));
        } else {
            transferTransaction.setTransactionType(TransactionType.EXT_TRANSFER);
            transferTransaction.setDescription(
                    String.format("External transfer: %s", transferDto.getDescription()));
        }

        transactionRepository.save(transferTransaction);
        accountRepository.save(senderAccount);
        accountRepository.save(recipientAccount);
    }
}
