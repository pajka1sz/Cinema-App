package agh.to.lab.cinema.model.purchases;

import agh.to.lab.cinema.model.seances.Seance;
import agh.to.lab.cinema.model.users.CinemaUser;
import lombok.Getter;
import lombok.Setter;

public class PurchaseDTO {
    @Setter
    @Getter
    private int user_id;
    @Setter
    @Getter
    private int seance_id;
    @Setter
    @Getter
    private int numberOfTickets;

    public PurchaseDTO(int user, int seance, Integer numberOfTickets) {
        this.user_id = user;
        this.seance_id = seance;
        this.numberOfTickets = numberOfTickets;
    }
}
