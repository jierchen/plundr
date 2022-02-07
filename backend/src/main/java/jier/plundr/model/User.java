package jier.plundr.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalDateTime;

@MappedSuperclass()
@Setter
@Getter
public class User {

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
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    // Login information
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;

    // Record information
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}
