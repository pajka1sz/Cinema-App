package agh.to.lab.cinema.model.types;

import agh.to.lab.cinema.model.movies.Movie;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Entity @Getter
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private MovieType movieType;

    @ManyToMany(mappedBy = "types")
    private Set<Movie> movies = new HashSet<>();

    public Type () {}

    public Type(MovieType movieType) {
        this.movieType = movieType;
    }

    @Override
    public String toString() {
        return movieType.toString();
    }
}
