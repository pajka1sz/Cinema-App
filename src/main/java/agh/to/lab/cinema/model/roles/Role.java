package agh.to.lab.cinema.model.roles;

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

    public Role(RoleType role) {
        this.role = role;
    }

    public RoleType getRole() {
        return role;
    }
}
