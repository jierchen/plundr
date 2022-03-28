package jier.plundr.service;

import jier.plundr.dto.transaction.CreateTransactionDTO;
import jier.plundr.model.Transaction;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TransactionService {

    List<Transaction> findAll(Pageable pageable);

    List<Transaction> findAllRelatedToAccount(Long accountId, Pageable pageable);

    Optional<Transaction> findById(Long transactionId);

    Transaction saveTransaction(Transaction transaction);

    Transaction createTransaction(CreateTransactionDTO createTransactionDto);

    Boolean deleteTransaction(Long transactionId);
}
