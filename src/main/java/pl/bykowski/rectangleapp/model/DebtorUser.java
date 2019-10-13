package pl.bykowski.rectangleapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

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
    private String permissions;
    private String email;
    private String authenticationCode = "";
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    public DebtorUser(String name, String password, Set<Role> roles, String permissions, String email, Integer active, String authenticationCode) {
        this.name = name;
        this.password = password;
        this.roles = roles;
        this.permissions = permissions;
        this.active = active;
        this.email = email;
        this.authenticationCode = authenticationCode;
    }
}