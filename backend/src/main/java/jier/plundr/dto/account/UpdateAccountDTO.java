package jier.plundr.dto.account;

import jier.plundr.model.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdateAccountDTO {

    private String name;
    private AccountType accountType;
}
