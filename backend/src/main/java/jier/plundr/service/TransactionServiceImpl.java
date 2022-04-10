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

    /**
     * Finds all {@code Transactions}.
     *
     * @param pageable {@code Pageable} object containing pagination information to limit search results.
     * @return Page information of found {@code Transactions}.
     */
    @Override
    public ReturnPageDTO<Transaction> findAll(Pageable pageable) {
        Page<Transaction> transactionPage = transactionRepository.findAll(pageable);
        return pageMapper.pageToReturnPageDTO(transactionPage);
    }

    /**
     * Finds all {@code Transactions} related to an {@code Account}.
     * @param accountId {@code id} of {@code Account}.
     * @param pageable {@code Pageable} object containing pagination information to limit search results.
     * @return Page information of found {@code Transactions}.
     */
    @Override
    public ReturnPageDTO<Transaction> findAllRelatedToAccount(Long accountId, Pageable pageable) {
        Account account = accountRepository.getById(accountId);

        Page<Transaction> transactionPage =
                transactionRepository.findAllByOwningAccountOrRecipientAccount(account, account, pageable);

        return pageMapper.pageToReturnPageDTO(transactionPage);
    }

    /**
     * Finds an {@code Transaction} by {@code id}.
     *
     * @param transactionId {@code id} of {@code Transaction} to find.
     * @return An {@code Optional} object containing the found {@code Transaction}
     *         or {@code null} if {@code Transaction} is not found.
     */
    @Override
    public Optional<Transaction> findById(Long transactionId) {
        return transactionRepository.findById(transactionId);
    }

    /**
     * Saves a {@code Transaction} to keep changes made.
     *
     * @param transaction {@code Transaction} to save.
     * @return {@code Transaction} saved.
     */
    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    /**
     * Creates and saves a new {@code Transaction}
     *
     * @param createTransactionDto {@code CreateTransactionDTO} containing information for {@code Transaction} creation
     * @return {@code Transaction} created and saved
     */
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

    /**
     * Deletes an existing {@code Transaction}.
     *
     * @param transactionId {@code id} of {@code Transaction} to delete.
     * @return Return {@code True} if found {@code Transaction} has been successfully deleted, {@code False} otherwise.
     */
    @Override
    public Boolean deleteTransaction(Long transactionId) {
        if(transactionRepository.existsById(transactionId)) {
            transactionRepository.deleteById(transactionId);
            return true;
        }
        return false;
    }
}
