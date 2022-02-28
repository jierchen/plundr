package jier.plundr.model;

import jier.plundr.model.enums.Province;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
    private String streetAddress;
    @Column(name = "city")
    private String city;
    @Column(name = "zip_code")
    private String zipCode;
    @Column(name = "province")
    private Province province;

    @OneToOne(mappedBy = "address")
    private User owningUser;
}
