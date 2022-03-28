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
public class TransferDTO {

    private Long senderAccountId;
    private Long recipientAccountId;
    private BigDecimal amount;
    private String Description;

}
