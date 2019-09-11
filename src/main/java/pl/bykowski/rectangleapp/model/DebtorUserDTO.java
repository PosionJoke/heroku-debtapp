package pl.bykowski.rectangleapp.model;

import lombok.Data;

@Data
public class DebtorUserDTO {
    private String name;
    private String email;
    private String password1;
    private String password2;
}
