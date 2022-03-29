package jier.plundr.controller;

import jier.plundr.model.Transaction;
import jier.plundr.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // ------------------------ Admin Requests -------------------------//

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions(@RequestParam int page, @RequestParam int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            List<Transaction> transactions = transactionService.findAll(pageable);

            return new ResponseEntity(transactions, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/transaction/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable("id") long transactionId) {
        try {
            Optional<Transaction> optionalTransaction = transactionService.findById(transactionId);

            if(optionalTransaction.isPresent()) {
                return new ResponseEntity<>(optionalTransaction.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // ------------------------ Customer Requests -------------------------//

    @GetMapping("/account/{id}/transactions")
    public ResponseEntity<List<Transaction>> getAccountTransactions(@PathVariable("id") long transactionId,
                                                                    @RequestParam int page,
                                                                    @RequestParam int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            List<Transaction> accountTransactions = transactionService.findAllRelatedToAccount(transactionId, pageable);

            return new ResponseEntity<>(accountTransactions, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
