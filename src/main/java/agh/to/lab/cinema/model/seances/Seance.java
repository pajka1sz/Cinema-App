package agh.to.lab.cinema.model.seances;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.purchases.Purchase;
import agh.to.lab.cinema.model.rooms.Room;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Seance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @OneToMany(mappedBy = "seance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Purchase> purchases;

    @Column
    private LocalDateTime startDate;

    @Column
    private float price;

    public Seance() {}

    public Seance(Movie movie, Room room, LocalDateTime startDate, float price) {
        this.movie = movie;
        this.room = room;
        this.startDate = startDate;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public float getPrice() {
        return price;
    }
}
