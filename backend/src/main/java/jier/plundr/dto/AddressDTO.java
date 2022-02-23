package jier.plundr.dto;

import jier.plundr.model.enums.Province;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AddressDTO {

    private String streetAddress;
    private String city;
    private String zipCode;
    private Province province;

}
