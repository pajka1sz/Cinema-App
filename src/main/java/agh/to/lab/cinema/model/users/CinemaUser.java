package agh.to.lab.cinema.model.users;

import agh.to.lab.cinema.model.purchases.Purchase;
import agh.to.lab.cinema.model.rates.MovieRate;
import agh.to.lab.cinema.model.roles.Role;
import agh.to.lab.cinema.model.roles.RoleType;
import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class CinemaUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieRate> ratings;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Purchase> purchases;

    public CinemaUser() {}

    public CinemaUser(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public RoleType getRoleType() {
        return (role != null) ? role.getRole() : RoleType.USER;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static boolean validateEmail(CinemaUser cinemaUser) {
        return cinemaUser.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static CinemaUser hashPassword(CinemaUser cinemaUser) {
        cinemaUser.password = BCrypt.hashpw(cinemaUser.password, BCrypt.gensalt());
        return cinemaUser;
    }
}
