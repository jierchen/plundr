package jier.plundr.service;

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

    @Override
    public List<Account> findAll(Pageable pageable) {
        Page<Account> accountPage = accountRepository.findAll(pageable);
        return accountPage.getContent();
    }

    @Override
    public List<Account> findAllByOwningCustomer(Long customerId) {
        Customer owningCustomer = customerRepository.getById(customerId);
        return accountRepository.findAllByOwningCustomer(owningCustomer);
    }

    @Override
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account createAccount(AccountType accountType, Long customerId) {
        Customer owningCustomer = customerRepository.getById(customerId);

        Account newAccount = new Account();
        newAccount.setAccountType(accountType);
        newAccount.setOwningCustomer(owningCustomer);
        newAccount.setBalance(new BigDecimal("0.00"));
        // newAccount.setTransactions(new ArrayList<Transaction>());

        AccountTypeData data = ACCOUNT_TYPE_MAP.get(accountType);

        if(data == null) {
            return null;
        } else {
            newAccount.setWithdrawFee(data.getWithdrawFee());
            newAccount.setInterestRate(data.getInterestRate());
        }

        return this.saveAccount(newAccount);
    }

    @Override
    public Account updateAccount(Long accountId, AccountType accountType) {
        Account account = accountRepository.getById(accountId);

        AccountTypeData data = ACCOUNT_TYPE_MAP.get(accountType);

        if(data == null) {
            return null;
        } else {
            account.setAccountType(accountType);
            account.setWithdrawFee(data.getWithdrawFee());
            account.setInterestRate(data.getInterestRate());
        }

        return accountRepository.save(account);
    }

    @Override
    public Boolean deleteAccount(Long accountId) {
        if(accountRepository.existsById(accountId)){
            accountRepository.deleteById(accountId);
            return true;
        }
        return false;
    }

    @Override
    public void deposit(Long accountId, BigDecimal amount) {
        assert amount.compareTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)) > 0;

        Account account = accountRepository.getById(accountId);
        account.setBalance(account.getBalance().add(amount));

        Transaction depositTransaction = new Transaction();
        depositTransaction.setTransactionType(TransactionType.DEPOSIT);
        depositTransaction.setAmount(amount);
        depositTransaction.setDescription("Deposit to account");
        depositTransaction.setOwningAccount(account);

        transactionRepository.save(depositTransaction);
        accountRepository.save(account);
    }

    @Override
    public void withdraw(Long accountId, BigDecimal amount) {
        assert amount.compareTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)) > 0;

        Account account = accountRepository.getById(accountId);
        account.setBalance(account.getBalance().subtract(amount));

        Transaction withdrawTransaction = new Transaction();
        withdrawTransaction.setTransactionType(TransactionType.WITHDRAW);
        withdrawTransaction.setAmount(amount);
        withdrawTransaction.setDescription("Withdraw from account");
        withdrawTransaction.setOwningAccount(account);

        transactionRepository.save(withdrawTransaction);
        accountRepository.save(account);
    }

    @Override
    public void transfer(Long senderAccountId, Long recipientAccountId, BigDecimal amount) {
        assert amount.compareTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)) > 0;

        Account senderAccount = accountRepository.getById(senderAccountId);
        Account recipientAccount = accountRepository.getById(recipientAccountId);
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
                    String.format("Internal transfer from account %s to account %s", senderAccount.getId(), recipientAccount.getId()));
        } else {
            transferTransaction.setTransactionType(TransactionType.EXT_TRANSFER);
            transferTransaction.setDescription(
                    String.format("External transfer from account %s to account %s", senderAccount.getId(), recipientAccount.getId()));
        }

        transactionRepository.save(transferTransaction);
        accountRepository.save(senderAccount);
        accountRepository.save(recipientAccount);
    }
}
