package agh.to.lab.cinema.model.roles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private RoleType role;

    public Role() {

    }

    @JsonCreator
    public Role(String role) {
        this.role = RoleType.valueOf(role.toUpperCase());
    }

    @JsonValue
    public String getRoleToString() {
        return role.name();
    }


    public Role(RoleType role) {
        this.role = role;
    }

    public RoleType getRole() {
        return role;
    }
}
