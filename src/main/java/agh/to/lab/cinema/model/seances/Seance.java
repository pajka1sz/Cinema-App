package agh.to.lab.cinema.model.seances;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.purchases.Purchase;
import agh.to.lab.cinema.model.rooms.Room;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Seance {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Getter
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @OneToMany(mappedBy = "seance", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @JsonIgnoreProperties(value = "seance", allowSetters = true)
    private List<Purchase> purchases;

    @Setter
    @Getter
    @Column
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startDate;

    @Getter
    @Setter
    @Column
    private float price;

    public Seance() {}

    public Seance(Movie movie, Room room, LocalDateTime startDate, float price) {
        this.movie = movie;
        this.room = room;
        this.startDate = startDate;
        this.price = price;
    }
}
