package jier.plundr.service;

import jier.plundr.dto.ReturnPageDTO;
import jier.plundr.dto.transaction.CreateTransactionDTO;
import jier.plundr.mapper.PageMapper;
import jier.plundr.model.Account;
import jier.plundr.model.Transaction;
import jier.plundr.repository.AccountRepository;
import jier.plundr.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private final TransactionRepository transactionRepository;

    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    private final PageMapper pageMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository, PageMapper pageMapper) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.pageMapper = pageMapper;
    }

    @Override
    public ReturnPageDTO<Transaction> findAll(Pageable pageable) {
        Page<Transaction> transactionPage = transactionRepository.findAll(pageable);
        return pageMapper.pageToReturnPageDTO(transactionPage);
    }

    @Override
    public ReturnPageDTO<Transaction> findAllRelatedToAccount(Long accountId, Pageable pageable) {
        Account account = accountRepository.getById(accountId);

        Page<Transaction> transactionPage =
                transactionRepository.findAllByOwningAccountOrRecipientAccount(account, account, pageable);

        return pageMapper.pageToReturnPageDTO(transactionPage);
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
