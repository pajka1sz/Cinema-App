package agh.to.lab.cinema.model.statistics;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.purchases.Purchase;
import agh.to.lab.cinema.model.rooms.Room;
import agh.to.lab.cinema.model.seances.Seance;
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
    public CinemaUser getUserWithMostReservation() {
        return cinemaUserStatisticsProvider.getUserWithMostReservation();
    }

    @Override
    public Long getReservationCountByUser(Long userId) {
        return cinemaUserStatisticsProvider.getReservationCountByUser(userId);
    }

    @Override
    public CinemaUser getUserWithHighestSpendings() {
        return cinemaUserStatisticsProvider.getUserWithHighestSpendings();
    }

    @Override
    public CinemaUser getUserWithMostRates() {
        return cinemaUserStatisticsProvider.getUserWithMostRates();
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

    @Override
    public Room getMostPopularRoom() {
        return roomStatisticsProvider.getMostPopularRoom();
    }

    @Override
    public Double getMovieRatesAvg(Movie movie) {
        return movieStatisticsProvider.getMovieRatesAvg(movie);
    }

    @Override
    public Map<Movie, Double> getMovieRatesAvgPerMovie() {
        List<Object[]> results = movieStatisticsProvider.getMovieRatesAvgPerMovie();
        return results.stream()
                .collect(Collectors.toMap(
                        row -> (Movie) row[0],
                        row -> (Double) row[1]
                ));
    }

    @Override
    public Movie getMostRatedMovie() {
        return movieStatisticsProvider.getMostRatedMovie();
    }

    @Override
    public Map<Movie, Double> getTop5MostRatedMovies() {
        List<Object[]> results = movieStatisticsProvider.getTop5MostRatedMovies();
        return results.stream()
                .collect(Collectors.toMap(
                        row -> (Movie) row[0],
                        row -> (Double) row[1]
                ));
    }

    @Override
    public Movie getBestRatedMovie() {
        return movieStatisticsProvider.getBestRatedMovie();
    }

    @Override
    public Movie getWorstRatedMovie() {
        return movieStatisticsProvider.getWorstRatedMovie();
    }

    @Override
    public Map<Movie, Double> getTop5BestRatedMovies() {
        List<Object[]> results = movieStatisticsProvider.getTop5BestRatedMovies();
        return results.stream()
                .collect(Collectors.toMap(
                        row -> (Movie) row[0],
                        row -> (Double) row[1]
                ));
    }

    @Override
    public Map<Movie, Double> getTop5WorstRatedMovies() {
        List<Object[]> results = movieStatisticsProvider.getTop5WorstRatedMovies();
        return results.stream()
                .collect(Collectors.toMap(
                        row -> (Movie) row[0],
                        row -> (Double) row[1]
                ));
    }

    @Override
    public Type getBestRatedMovieType() {
        return movieStatisticsProvider.getBestRatedMovieType();
    }


    @Override
    public Movie getBestMovieByType(Type type) {
        return movieStatisticsProvider.getBestMovieByType(type);
    }

    @Override
    public Map<Movie, Double> getTop5BestMoviesByType(Type type) {
        List<Object[]> results = movieStatisticsProvider.getTop5BestMoviesByType(type);
        return results.stream()
                .collect(Collectors.toMap(
                        row -> (Movie) row[0],
                        row -> (Double) row[1]
                ));
    }

    @Override
    public Map<Type, Movie> getBestMoviePerType() {
        List<Object[]> results = movieStatisticsProvider.getBestMoviePerType();
        return results.stream()
                .collect(Collectors.toMap(
                        row -> (Type) row[0],
                        row -> (Movie) row[1]
                ));
    }

    @Override
    public Map<Room, Integer> getTop5RoomsWithMostSeances() {
        List<Object[]> results = roomStatisticsProvider.getTop5RoomsWithMostSeances();
        return results.stream()
                .collect(Collectors.toMap(
                        row -> (Room) row[0],
                        row -> (Integer) row[1]
                ));
    }

    @Override
    public Room getRoomWithMostSeances() {
        return roomStatisticsProvider.getRoomWithMostSeances();
    }

    @Override
    public List<Seance> getFutureSeancesInRoom(Room room, LocalDateTime start) {
        return roomStatisticsProvider.getFutureSeancesInRoom(room, start);
    }
}
