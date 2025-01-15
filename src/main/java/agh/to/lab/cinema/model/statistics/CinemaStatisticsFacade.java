package agh.to.lab.cinema.model.statistics;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.purchases.Purchase;
import agh.to.lab.cinema.model.rooms.Room;
import agh.to.lab.cinema.model.seances.Seance;
import agh.to.lab.cinema.model.types.Type;
import agh.to.lab.cinema.model.users.CinemaUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface CinemaStatisticsFacade {
    CinemaUser getUserWithMostReservation();
    Long getReservationCountByUser(Long userId);
    CinemaUser getUserWithHighestSpendings();
    Double getRevenueFromMovie(Movie movie);
    Map<Movie, Integer> getSumOfTicketsPerMovie();
    List<Type> getTop3MostPopularTypes();
    List<Purchase> findLargePurchases(BigDecimal amount);
    Double getAverageAmountOfTicketsRecently(LocalDateTime start, LocalDateTime end);
    Map<Room, Double> getAverageOccupancyPerRoom();
    Room getMostPopularRoom();
    Double getMovieRatesAvg(Movie movie);
    Map<Movie, Double> getMovieRatesAvgPerMovie();
    Movie getMostRatedMovie();
    Map<Movie, Double> getTop5MostRatedMovies();
    Movie getBestRatedMovie();
    Movie getWorstRatedMovie();
    Map<Movie, Double> getTop5BestRatedMovies();
    Map<Movie, Double> getTop5WorstRatedMovies();
    Type getBestRatedMovieType();
    CinemaUser getUserWithMostRates();
    Movie getBestMovieByType(Type type);
    Map<Movie, Double> getTop5BestMoviesByType(Type type);
    Map<Type, Movie> getBestMoviePerType();
    Map<Room, Integer> getTop5RoomsWithMostSeances();
    Room getRoomWithMostSeances();
    List<Seance> getFutureSeancesInRoom(Room room, LocalDateTime start);
}
