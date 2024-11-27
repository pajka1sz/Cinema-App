package agh.to.lab.cinema.model.users;

import agh.to.lab.cinema.model.rates.MovieRate;
import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

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

    public CinemaUser() {}

    public CinemaUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
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

    public static boolean validateEmail(CinemaUser cinemaUser) {
        return cinemaUser.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static CinemaUser hashPassword(CinemaUser cinemaUser) {
        cinemaUser.password = BCrypt.hashpw(cinemaUser.password, BCrypt.gensalt());
        return cinemaUser;
    }
}
