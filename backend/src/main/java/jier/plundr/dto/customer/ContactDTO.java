package jier.plundr.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContactDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

}
