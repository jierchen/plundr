package jier.plundr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jier.plundr.model.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "plundr_user")
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public abstract class PlundrUser extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    @NotNull
    private UserType type;

    // Personal information
    @Column(name = "first_name")
    @NotEmpty
    @Size(max = 20)
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty
    @Size(max = 20)
    private String lastName;

    @Column(name = "phone_number")
    @Size(max = 10)
    private String phoneNumber;

    @Column(name = "date_of_birth")
    @NotNull
    private LocalDate dateOfBirth;

    @Column(name = "email")
    @NotEmpty
    @Email
    private String email;

    // Address information
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "address_id")
    @JsonManagedReference
    private Address address;

    // Login information
    @Column(name = "username")
    @NotEmpty
    @Size(max = 20)
    private String username;

    @Column(name = "password")
    @NotEmpty
    @Size(max = 64)
    @JsonIgnore
    private String password;
}
