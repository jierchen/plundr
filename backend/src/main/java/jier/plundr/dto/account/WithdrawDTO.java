package jier.plundr.dto.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class WithdrawDTO {

    private Long accountId;
    private BigDecimal amount;
    private String description;
}
