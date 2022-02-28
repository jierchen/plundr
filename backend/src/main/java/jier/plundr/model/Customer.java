package jier.plundr.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customer")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Customer extends User {

    // Customer bank accounts
    @OneToMany(mappedBy = "owningCustomer", cascade = CascadeType.ALL)
    private List<Account> accounts;
    @OneToOne
    @JoinColumn(name = "deposit_id")
    private Account depositAccount;

    // Contacts for transfers
    @ManyToMany
    @JoinTable(
            name = "contacts",
            joinColumns =  @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id")
    )
    private List<Customer> contacts;
}
