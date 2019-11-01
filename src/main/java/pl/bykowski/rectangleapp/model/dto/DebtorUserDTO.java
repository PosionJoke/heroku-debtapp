package pl.bykowski.rectangleapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bykowski.rectangleapp.annotation.PasswordCheck;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PasswordCheck(message = "Passwords must be equals")
public class DebtorUserDTO {
    @NotEmpty
    @Size(min = 2, message = "Name should have at least 2 characters")
    private String name;
    @Email
    private String email;
    @NotEmpty
    private String password1;
    @NotEmpty
    private String password2;
    private String authenticationCodeInput;
    private String authenticationCode;

    private boolean test = false;
}
