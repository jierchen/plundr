package jier.plundr.service;

import jier.plundr.dto.transaction.CreateTransactionDTO;
import jier.plundr.model.Account;
import jier.plundr.model.Transaction;
import jier.plundr.repository.AccountRepository;
import jier.plundr.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private final TransactionRepository transactionRepository;

    @Autowired
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Transaction> findAll(Pageable pageable) {
        Page<Transaction> transactionPage = transactionRepository.findAll(pageable);
        return transactionPage.getContent();
    }

    @Override
    public List<Transaction> findAllRelatedToAccount(Long accountId, Pageable pageable) {
        Account account = accountRepository.getById(accountId);

        Page<Transaction> transactionPage =
                transactionRepository.findAllByOwningAccountOrRecipientAccount(account, account, pageable);

        return transactionPage.getContent();
    }

    @Override
    public Optional<Transaction> findById(Long transactionId) {
        return transactionRepository.findById(transactionId);
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction createTransaction(CreateTransactionDTO createTransactionDto) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(createTransactionDto.getTransactionType());
        transaction.setAmount(createTransactionDto.getAmount());
        transaction.setDescription(createTransactionDto.getDescription());

        if(createTransactionDto.getOwningAccountId() != null) {
            Account owningAccount = accountRepository.getById(createTransactionDto.getOwningAccountId());
            transaction.setOwningAccount(owningAccount);
        }

        if(createTransactionDto.getRecipientAccountId() != null) {
            Account recipientAccountIdAccount = accountRepository.getById(createTransactionDto.getRecipientAccountId());
            transaction.setOwningAccount(recipientAccountIdAccount);
        }

        return this.saveTransaction(transaction);
    }

    @Override
    public Boolean deleteTransaction(Long transactionId) {
        if(transactionRepository.existsById(transactionId)) {
            transactionRepository.deleteById(transactionId);
            return true;
        }
        return false;
    }
}
