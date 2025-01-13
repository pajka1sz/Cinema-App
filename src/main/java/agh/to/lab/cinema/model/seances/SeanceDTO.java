package agh.to.lab.cinema.model.seances;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.rooms.Room;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class SeanceDTO {
    @Getter
    @Setter
    private int movie_id;
    @Getter
    @Setter
    private int room_id;
    @Getter
    @Setter
    private LocalDateTime startDate;
    @Getter
    @Setter
    private float price;

    public SeanceDTO(int movie, int room, LocalDateTime startDate, float price) {
        this.movie_id = movie;
        this.room_id = room;
        this.startDate = startDate;
        this.price = price;
    }
}
