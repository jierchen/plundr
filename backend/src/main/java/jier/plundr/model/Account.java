package jier.plundr.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jier.plundr.model.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties({"transactions"})
public class Account extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private Long id;

    // Bank account information
    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    @NotNull
    private AccountType accountType;

    @Column(name = "name")
    @NotEmpty
    @Size(max = 20)
    private String name;

    @Column(name = "balance")
    @NotNull
    @Digits(integer = 8, fraction = 2)
    @PositiveOrZero
    private BigDecimal balance;

    @Column(name = "interest_rate")
    @NotNull
    @Digits(integer = 1, fraction = 4)
    @PositiveOrZero
    private BigDecimal interestRate;

    @Column(name = "withdraw_fee")
    @NotNull
    @Digits(integer = 3, fraction = 2)
    @PositiveOrZero
    private BigDecimal withdrawFee;

    @ManyToOne
    @JoinColumn(name="customer_id")
    @JsonBackReference
    @NotNull
    private Customer owningCustomer;

    @OneToMany(mappedBy = "owningAccount", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();
}