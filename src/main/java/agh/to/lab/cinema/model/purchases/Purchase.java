package agh.to.lab.cinema.model.purchases;

import agh.to.lab.cinema.model.seances.Seance;
import agh.to.lab.cinema.model.users.CinemaUser;
import jakarta.persistence.*;

@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private CinemaUser user;

    @ManyToOne
    @JoinColumn(name = "seance_id", nullable = false)
    private Seance seance;

    public Purchase() {};

    public Purchase(CinemaUser user, Seance seance) {
        this.user = user;
        this.seance = seance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CinemaUser getUser() {
        return user;
    }

    public void setUser(CinemaUser user) {
        this.user = user;
    }

    public Seance getSeance() {
        return seance;
    }

    public void setSeance(Seance seance) {
        this.seance = seance;
    }
}
