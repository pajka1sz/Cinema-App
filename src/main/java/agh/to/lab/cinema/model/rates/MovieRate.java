package agh.to.lab.cinema.model.rates;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.users.CinemaUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class MovieRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private CinemaUser user;

    @Column(nullable = false)
    private Integer rate;

    @Column
    private String description;

    public MovieRate() {}

    public MovieRate(Movie movie, CinemaUser user, Integer rate, String description) {
        this.movie = movie;
        this.user = user;
        this.rate = rate;
        this.description = description;
    }
}
