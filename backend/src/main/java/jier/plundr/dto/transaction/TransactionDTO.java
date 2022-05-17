package jier.plundr.dto.transaction;

import jier.plundr.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TransactionDTO {

    private Long id;

    private BigDecimal amount;
    private TransactionType transactionType;
    private String description;
    private Long owningAccountId;
    private Long recipientAccountId;

    private LocalDate createDate;

}
