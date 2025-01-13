package agh.to.lab.cinema.model.movies;

import agh.to.lab.cinema.model.rates.MovieRate;
import agh.to.lab.cinema.model.seances.Seance;
import agh.to.lab.cinema.model.types.Type;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
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

    @Column(nullable = false)
    private String thumbnail;

    @ManyToMany
    @JoinTable(
            name = "movie_types",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    private Set<Type> types = new HashSet<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieRate> ratings;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = "movie", allowSetters = true)
    private List<Seance> seances;

    public Movie() {}

    public Movie(String title, String description, Integer length, String thumbnail) {
        this.title = title;
        this.description = description;
        this.length = length;
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", length=" + length +
                ", thumbnail='" + thumbnail + '\'' +
                ", types=" + types +
                '}';
    }

    public void setTypes(Set<Type> types) {
        clearTypes(this.types);
        for (Type type : types) {
            addType(type);
        }
    }

    private void addType(Type type) {
        this.types.add(type);
        type.getMovies().add(this);
    }

    private void clearTypes(Set<Type> types) {
        Iterator<Type> typeIterator = this.types.iterator();
        while (typeIterator.hasNext()) {
            Type type = typeIterator.next();
            typeIterator.remove();
            type.getMovies().remove(this);
        }
    }
}
