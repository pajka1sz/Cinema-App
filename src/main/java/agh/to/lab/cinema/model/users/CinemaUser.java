package agh.to.lab.cinema.model.users;

import agh.to.lab.cinema.model.purchases.Purchase;
import agh.to.lab.cinema.model.rates.MovieRate;
import agh.to.lab.cinema.model.roles.Role;
import agh.to.lab.cinema.model.roles.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class CinemaUser {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(unique = true, nullable = false)
    private String username;

    @Getter
    @Column(unique = true, nullable = false)
    private String email;

    @Getter
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieRate> ratings;

    @Getter
    @Column(nullable = false)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = "user", allowSetters = true)
    private List<Purchase> purchases;

    public CinemaUser() {}

    public CinemaUser(String username, String password, String email, Role role) {
        this.username = username;
        this.password = hashPassword(password);
        this.email = email;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }

    public static boolean validateEmail(CinemaUser cinemaUser) {
        return cinemaUser.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public String toString() {
        return "CinemaUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
