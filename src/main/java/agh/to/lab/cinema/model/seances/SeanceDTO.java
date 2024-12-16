package agh.to.lab.cinema.model.seances;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.rooms.Room;

import java.time.LocalDateTime;

public class SeanceDTO {
    private Movie movie;
    private Room room;
    private LocalDateTime startDate;
    private float price;

    public SeanceDTO(Movie movie, Room room, LocalDateTime startDate, float price) {
        this.movie = movie;
        this.room = room;
        this.startDate = startDate;
        this.price = price;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
