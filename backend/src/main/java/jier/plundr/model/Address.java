package jier.plundr.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jier.plundr.model.enums.Province;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "address")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Address extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "address_id")
    private Long id;

    // Address information
    @Column(name = "street_address")
    @NotEmpty
    private String streetAddress;

    @Column(name = "city")
    @NotEmpty
    private String city;

    @Column(name = "zip_code")
    @NotEmpty
    private String zipCode;

    @Column(name = "province")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Province province;

    @OneToOne(mappedBy = "address")
    @NotNull
    @JsonBackReference
    private User owningUser;
}
