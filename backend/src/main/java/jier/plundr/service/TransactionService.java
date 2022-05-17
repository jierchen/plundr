package jier.plundr.service;

import jier.plundr.dto.ReturnPageDTO;
import jier.plundr.dto.transaction.CreateTransactionDTO;
import jier.plundr.dto.transaction.TransactionDTO;
import jier.plundr.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TransactionService {

    ReturnPageDTO<Transaction> findAll(Pageable pageable);

    ReturnPageDTO<TransactionDTO> findAllRelatedToAccount(Long accountId, Pageable pageable);

    Optional<Transaction> findById(Long transactionId);

    Transaction saveTransaction(Transaction transaction);

    Transaction createTransaction(CreateTransactionDTO createTransactionDto);

    Boolean deleteTransaction(Long transactionId);
}
