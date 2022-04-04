package jier.plundr.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jier.plundr.model.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public abstract class User extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private UserType type;

    // Personal information
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(name = "email")
    private String email;

    // Address information
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "address_id")
    @JsonManagedReference
    private Address address;

    // Login information
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
}
