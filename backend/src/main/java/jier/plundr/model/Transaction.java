package jier.plundr.model;

import jier.plundr.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transaction")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "transaction_id")
    private Long id;

    // Main transaction details
    @Column(name = "amount")
    @Digits(integer = 7, fraction = 2)
    @PositiveOrZero
    private BigDecimal amount;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    @NotNull
    private TransactionType transactionType;

    @Column(name = "description")
    @NotEmpty
    @Size(max = 100)
    private String description;

    @ManyToOne
    @JoinColumn(name="account_id")
    @NotNull
    private Account owningAccount;

    // Transfer information
    @ManyToOne
    @JoinColumn(name="recipient_id")
    private Account recipientAccount;
}
