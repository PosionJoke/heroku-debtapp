package pl.bykowski.rectangleapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class DebtorUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private float debt;
    private long timeOfDebt;
    private String password;
    private Integer active;
    private String roles;
    private String permissions;
    private String email;
    private String authenticationCode = "";

    public DebtorUser(String name, String password, String roles, String permissions, String email, Integer active, String authenticationCode) {
        this.name = name;
        this.password = password;
        this.roles = roles;
        this.permissions = permissions;
        this.active = active;
        this.email = email;
        this.authenticationCode = authenticationCode;
    }

    public List<String> getRoleList() {
        if (this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

    public List<String> getPermissionList() {
        if (this.permissions.length() > 0) {
            return Arrays.asList(this.permissions.split(","));
        }
        return new ArrayList<>();
    }
}