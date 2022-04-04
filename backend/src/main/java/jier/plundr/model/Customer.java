package jier.plundr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
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
    @JsonManagedReference
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
    @JsonIgnoreProperties("contacts")
    private List<Customer> contacts;
}
