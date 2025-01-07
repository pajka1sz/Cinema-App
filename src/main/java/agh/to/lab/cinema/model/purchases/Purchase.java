package agh.to.lab.cinema.model.purchases;

import agh.to.lab.cinema.model.seances.Seance;
import agh.to.lab.cinema.model.users.CinemaUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Purchase {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Getter
    @Setter
    private Integer numberOfTickets;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties(value = "purchase", allowSetters = true)
    private CinemaUser user;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "seance_id", nullable = false)
    private Seance seance;

    public Purchase() {};

    public Purchase(Integer numberOfTickets, CinemaUser user, Seance seance) {
        this.user = user;
        this.seance = seance;
        this.numberOfTickets = numberOfTickets;
    }
}
