package agh.to.lab.cinema.model.purchases;

import agh.to.lab.cinema.model.seances.Seance;
import agh.to.lab.cinema.model.users.CinemaUser;

public class PurchaseDTO {
    private CinemaUser user;
    private Seance seance;

    public PurchaseDTO(CinemaUser user, Seance seance) {
        this.user = user;
        this.seance = seance;
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
