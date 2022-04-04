package jier.plundr.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jier.plundr.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private BigDecimal amount;
    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name="account_id")
    @JsonBackReference
    private Account owningAccount;

    // Transfer information
    @ManyToOne
    @JoinColumn(name="recipient_id")
    @JsonBackReference
    private Account recipientAccount;
}
