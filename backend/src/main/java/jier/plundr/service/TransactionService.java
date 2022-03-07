package jier.plundr.service;

import jier.plundr.dto.TransactionDTO;
import jier.plundr.model.Account;
import jier.plundr.model.Transaction;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {

    List<Transaction> findAll(Pageable pageable);

    List<Transaction> findAllRelatedToAccount(Long accountId, Pageable pageable);

    Transaction saveTransaction(Transaction transaction);

    Transaction createTransaction(TransactionDTO transactionDto, Long owningAccountId, Long recipientAccountId);

    Boolean deleteTransaction(Long transactionId);
}
