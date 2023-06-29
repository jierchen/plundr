package jier.plundr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Constraint;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "customer")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties({"accounts", "contacts"})
public class Customer extends PlundrUser {

    // Customer bank accounts
    @OneToMany(mappedBy = "owningCustomer", cascade = CascadeType.ALL)
    @Size(max = 5)
    private List<Account> accounts = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "deposit_id")
    @JsonManagedReference
    private Account depositAccount;

    // Contacts for transfers
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "contacts",
            joinColumns =  @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id")
    )
    @Size(max = 100)
    private Set<Customer> contacts = new HashSet<>();
}
