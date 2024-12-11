package agh.to.lab.cinema.model.types;

import agh.to.lab.cinema.model.movies.Movie;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private MovieType movieType;

    @ManyToMany(mappedBy = "types")
    private Set<Movie> movies;

    public Type () {}

    public Type(MovieType movieType) {
        this.movieType = movieType;
    }
}
