package jier.plundr.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jier.plundr.model.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Account extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private Long id;

    // Bank account information
    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @Column(name = "balance")
    private BigDecimal balance;
    @Column(name = "interest_rate")
    private BigDecimal interestRate;
    @Column(name = "withdraw_fee")
    private BigDecimal withdrawFee;

    @ManyToOne
    @JoinColumn(name="customer_id")
    @JsonBackReference
    private Customer owningCustomer;

    @OneToMany(mappedBy = "owningAccount", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Transaction> Transactions = new ArrayList<>();
}