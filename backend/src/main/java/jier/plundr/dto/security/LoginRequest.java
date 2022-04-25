package jier.plundr.dto.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Getter
@Setter
public class LoginRequest {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
