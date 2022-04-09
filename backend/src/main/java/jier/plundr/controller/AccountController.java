package jier.plundr.controller;

import jier.plundr.dto.ReturnPageDTO;
import jier.plundr.dto.account.*;
import jier.plundr.model.Account;
import jier.plundr.service.AccountService;
import jier.plundr.service.CustomerService;
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
public class AccountController {

    @Autowired
    private final AccountService accountService;

    @Autowired
    private final CustomerService customerService;


    public AccountController(AccountService accountService, CustomerService customerService) {
        this.accountService = accountService;
        this.customerService = customerService;
    }

    // ------------------------ Admin Requests -------------------------//

    @GetMapping("/accounts")
    public ResponseEntity<ReturnPageDTO<Account>> getAllAccounts(@RequestParam int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            ReturnPageDTO<Account> accountsReturnPage = accountService.findAll(pageable);

            return new ResponseEntity<>(accountsReturnPage, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable("id") long accountId) {
        try {
            Optional<Account> optionalAccount = accountService.findById(accountId);

            if(optionalAccount.isPresent()) {
                return new ResponseEntity<>(optionalAccount.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/account")
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountDTO createAccountDto) {
        try {
            Account newAccount = accountService.createAccount(createAccountDto);

            return new ResponseEntity<>(newAccount, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/account/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable("id") long accountId,
                                                 @RequestBody UpdateAccountDTO updateAccountDto) {
        try {
            Account account = accountService.updateAccount(accountId, updateAccountDto);

            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/account/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("id") long accountId) {
        try {
            Boolean isDeleted = accountService.deleteAccount(accountId);

            if(isDeleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // ------------------------ Customer Requests -------------------------//

    @GetMapping("/customer/{id}/accounts")
    public ResponseEntity<List<Account>> getCustomerAccounts(@PathVariable("id") long customerId) {
        try {
            List<Account> customerAccounts = accountService.findAllByOwningCustomer(customerId);

            return new ResponseEntity<>(customerAccounts, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/customer/{id}/setDepositAccount")
    public ResponseEntity<Void> setDepositAccount(@PathVariable("id") long customerId,
                                                  @RequestBody SetDepositAccountDTO setDepositAccountDto) {
        try {
            accountService.setDepositAccount(customerId, setDepositAccountDto.getAccountId());

            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/account/deposit")
    public ResponseEntity<Void> deposit(@RequestBody DepositDTO depositDTO) {
        try {
            accountService.deposit(depositDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/account/withdraw")
    public ResponseEntity<Void> withdraw(@RequestBody WithdrawDTO withdrawDTO) {
        try {
            accountService.withdraw(withdrawDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/account/transfer")
    public ResponseEntity<Void> transfer(@RequestBody TransferDTO transferDTO) {
        try {
            accountService.transfer(transferDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
