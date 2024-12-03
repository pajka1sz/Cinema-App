package agh.to.lab.cinema.model.movies;

import agh.to.lab.cinema.model.rates.MovieRate;
import agh.to.lab.cinema.model.seances.Seance;
import agh.to.lab.cinema.model.types.Type;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column()
    private String description;

    @Column(nullable = false)
    private Integer length;

    @ManyToMany
    @JoinTable(
            name = "movie_types",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    private Set<Type> types;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieRate> ratings;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seance> seances;

    public Movie() {}

    public Movie(String title, String description, Integer length) {
        this.title = title;
        this.description = description;
        this.length = length;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getLength() {
        return length;
    }
}
