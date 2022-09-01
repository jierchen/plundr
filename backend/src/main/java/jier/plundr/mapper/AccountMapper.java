package jier.plundr.mapper;

import jier.plundr.dto.account.AccountDTO;
import jier.plundr.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    /**
     * Maps {@code Account} to {@code AccountDTO}.
     * @param account {@code Account} to map.
     * @param isDepositAccount Whether {@code Account} is the deposit account.
     * @return {@code AccountDTO} mapped.
     */
    public AccountDTO accountToAccountDTO(Account account, Boolean isDepositAccount) {
        AccountDTO accountDTO = new AccountDTO();

        accountDTO.setId(account.getId());
        accountDTO.setAccountType(account.getAccountType());
        accountDTO.setName(account.getName());
        accountDTO.setBalance(account.getBalance());
        accountDTO.setWithdrawFee(account.getWithdrawFee());
        accountDTO.setInterestRate(account.getInterestRate());

        accountDTO.setIsDepositAccount(isDepositAccount);

        return accountDTO;
    }
}
