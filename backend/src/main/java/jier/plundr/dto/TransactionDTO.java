package jier.plundr.dto;

import jier.plundr.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TransactionDTO {

    private BigDecimal amount;
    private TransactionType transactionType;
    private String description;

}
