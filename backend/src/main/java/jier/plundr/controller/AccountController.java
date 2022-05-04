package jier.plundr.controller;

import jier.plundr.dto.ReturnPageDTO;
import jier.plundr.dto.account.*;
import jier.plundr.model.Account;
import jier.plundr.security.UserDetailsImpl;
import jier.plundr.service.AccountService;
import jier.plundr.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    // ------------------------ Admin Requests -------------------------//

    @GetMapping("/admin/accounts")
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

    @GetMapping("admin/account/{id}")
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

    @PostMapping("admin/customer/{customerId}/account")
    public ResponseEntity<Account> createAccount(@PathVariable("customerId") long customerId,
                                                 @RequestBody CreateAccountDTO createAccountDto) {
        try {
            Account newAccount = accountService.createAccount(customerId, createAccountDto);

            return new ResponseEntity<>(newAccount, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("admin/customer/{customerId}/account/{accountId}")
    public ResponseEntity<Account> updateAccount(@PathVariable("customerId") long customerId,
                                                 @PathVariable("accountId") long accountId,
                                                 @RequestBody UpdateAccountDTO updateAccountDto) {
        try {
            Account account = accountService.updateAccount(customerId, accountId, updateAccountDto);

            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("admin/customer/{customerId}/account/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("customerId") long customerId,
                                              @PathVariable("accountId") long accountId) {
        try {
            Boolean isDeleted = accountService.deleteAccount(customerId, accountId);

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

    @PostMapping("/account")
    public ResponseEntity<Account> createCustomerAccount(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @RequestBody CreateAccountDTO createAccountDto) {
        try {
            Account newAccount = accountService.createAccount(userDetails.getId(), createAccountDto);

            return new ResponseEntity<>(newAccount, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/account/{id}")
    public ResponseEntity<Account> updateCustomerAccount(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @PathVariable("id") long accountId,
                                                 @RequestBody UpdateAccountDTO updateAccountDto) {
        try {
            Account account = accountService.updateAccount(userDetails.getId(), accountId, updateAccountDto);

            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/account/{id}")
    public ResponseEntity<Void> deleteCustomerAccount(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @PathVariable("id") long accountId) {
        try {
            Boolean isDeleted = accountService.deleteAccount(userDetails.getId(), accountId);

            if(isDeleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getCustomerAccounts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            List<Account> customerAccounts = accountService.findAllByOwningCustomer(userDetails.getId());

            return new ResponseEntity<>(customerAccounts, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("setDepositAccount")
    public ResponseEntity<Void> setCustomerDepositAccount(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                          @RequestBody SetDepositAccountDTO setDepositAccountDto) {
        try {
            accountService.setDepositAccount(userDetails.getId(), setDepositAccountDto.getAccountId());

            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/account/{id}/deposit")
    public ResponseEntity<Void> depositToAccount(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @PathVariable("id") long accountId,
                                                 @RequestBody DepositDTO depositDTO) {
        try {
            accountService.deposit(userDetails.getId(), accountId, depositDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/account/{id}/withdraw")
    public ResponseEntity<Void> withdrawFromAccount(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                    @PathVariable("id") long accountId,
                                                    @RequestBody WithdrawDTO withdrawDTO) {
        try {
            accountService.withdraw(userDetails.getId(), accountId, withdrawDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/account/{id}/transfer")
    public ResponseEntity<Void> transferBetweenAccounts(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @PathVariable("id") long accountId,
                                                        @RequestBody TransferDTO transferDTO) {
        try {
            accountService.transfer(userDetails.getId(), accountId, transferDTO);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
