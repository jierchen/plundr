package jier.plundr.model;

import jier.plundr.model.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Account {

    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private Long id;

    // Bank account information
    @Column(name = "account_type")
    private AccountType accountType;
    @Column(name = "balance")
    private BigDecimal balance;
    @Column(name = "interest_rate")
    private BigDecimal interestRate;
    @Column(name = "withdraw_fee")
    private BigDecimal withdrawFee;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer owningCustomer;

    @OneToMany(mappedBy = "owningAccount", cascade = CascadeType.ALL)
    private List<Transaction> Transactions;

    // Record information
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}