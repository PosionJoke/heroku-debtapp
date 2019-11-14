package pl.bykowski.rectangleapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String authenticationCode;
    @Size(max = 7, message = "code must have at least 7 characters")
    @NotEmpty
    private String authenticationCodeInput;
    private String name;
    private String password1;
    private String password2;
}
