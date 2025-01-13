package agh.to.lab.cinema.model.statistics;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.purchases.Purchase;
import agh.to.lab.cinema.model.rooms.Room;
import agh.to.lab.cinema.model.types.Type;
import agh.to.lab.cinema.model.users.CinemaUser;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface CinemaStatisticsFacade {
    CinemaUser getMostActiveUser();
    Double getRevenueFromMovie(Movie movie);
    Map<Movie, Integer> getSumOfTicketsPerMovie();
    List<Type> getTop3MostPopularTypes();
    List<Purchase> findLargePurchases(BigDecimal amount);
    Double getAverageAmountOfTicketsRecently(LocalDateTime start, LocalDateTime end);
    Map<Room, Double> getAverageOccupancyPerRoom();
}
