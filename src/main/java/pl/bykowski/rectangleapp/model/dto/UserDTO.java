package pl.bykowski.rectangleapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String authenticationCode;
    private String authenticationCodeInput;
    private String name;
    private String password1;
    private String password2;
}
