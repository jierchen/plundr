package jier.plundr.dto.account;

import jier.plundr.model.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AccountDTO {

    private Long id;

    private AccountType accountType;
    private String name;
    private BigDecimal balance;
    private BigDecimal interestRate;
    private BigDecimal withdrawFee;
    private Boolean isDepositAccount;
}
