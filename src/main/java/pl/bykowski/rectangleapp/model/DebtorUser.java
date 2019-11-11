package pl.bykowski.rectangleapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DebtorUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private float debt;
    private long timeOfDebt;
    private String password;
    @Builder.Default
    private Integer active = 0;
    @Builder.Default
    private String permissions = "";
    private String email;
    private String authenticationCode;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "debotUser_friends",
            joinColumns = @JoinColumn(
                    name = "debtorUser_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "friendUsed_id", referencedColumnName = "id"))
    private Set<DebtorUser> friendsList;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;
}