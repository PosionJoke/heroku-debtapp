package pl.bykowski.rectangleapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String authenticationCode;
    @Size(max = 7, message = "code must have at least 7 characters")
    private String authenticationCodeInput;
    private String name;
    private String password1;
    private String password2;
}
