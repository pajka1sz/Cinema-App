package agh.to.lab.cinema.model.statistics;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.purchases.Purchase;
import agh.to.lab.cinema.model.rooms.Room;
import agh.to.lab.cinema.model.types.Type;
import agh.to.lab.cinema.model.users.CinemaUser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CinemaStatisticsImpl implements CinemaStatisticsFacade{
    private final CinemaUserStatisticsProvider cinemaUserStatisticsProvider;
    private final MovieStatisticsProvider movieStatisticsProvider;
    private final PurchasesStatisticsProvider purchasesStatisticsProvider;
    private final RoomStatisticsProvider roomStatisticsProvider;

    public CinemaStatisticsImpl(CinemaUserStatisticsProvider cinemaUserStatisticsProvider,
                                MovieStatisticsProvider movieStatisticsProvider,
                                PurchasesStatisticsProvider purchasesStatisticsProvider,
                                RoomStatisticsProvider roomStatisticsProvider) {
        this.cinemaUserStatisticsProvider = cinemaUserStatisticsProvider;
        this.movieStatisticsProvider = movieStatisticsProvider;
        this.purchasesStatisticsProvider = purchasesStatisticsProvider;
        this.roomStatisticsProvider = roomStatisticsProvider;
    }


    @Override
    public CinemaUser getMostActiveUser() {
        return cinemaUserStatisticsProvider.getMostActiveUser();
    }

    @Override
    public Double getRevenueFromMovie(Movie movie) {
        return movieStatisticsProvider.getRevenueFromMovie(movie);
    }

    @Override
    public Map<Movie, Integer> getSumOfTicketsPerMovie() {
        List<Object[]> results = movieStatisticsProvider.getSumOfTicketsPerMovie();
        return results.stream()
                .collect(Collectors.toMap(
                        row -> (Movie) row[0],
                        row -> ((Number) row[1]).intValue()
                ));
    }

    @Override
    public List<Type> getTop3MostPopularTypes() {
        return movieStatisticsProvider.getTop3MostPopularTypes();
    }

    @Override
    public List<Purchase> findLargePurchases(BigDecimal amount) {
        return purchasesStatisticsProvider.findLargePurchases(amount);
    }

    @Override
    public Double getAverageAmountOfTicketsRecently(LocalDateTime start, LocalDateTime end) {
        return purchasesStatisticsProvider.getAverageAmountOfTicketsRecently(start, end);
    }

    @Override
    public Map<Room, Double> getAverageOccupancyPerRoom() {
        List<Object[]> results = roomStatisticsProvider.getAverageOccupancyPerRoom();
        return results.stream()
                .collect(Collectors.toMap(
                        row -> (Room) row[0],
                        row -> (Double) row[1]
                ));
    }
}
