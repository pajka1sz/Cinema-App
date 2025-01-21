package agh.to.lab.cinema.restController;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.statistics.CinemaStatisticsService;
import agh.to.lab.cinema.model.users.CinemaUser;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping(path = "/stats")
public class StatisticsController {
    @Getter
    private static final String baseUrl = "http://localhost:8080/stats";
    private final CinemaStatisticsService cinemaStatisticsService;

    public StatisticsController(CinemaStatisticsService cinemaStatisticsService) {
        this.cinemaStatisticsService = cinemaStatisticsService;
    }

    @GetMapping("/movie_gross/{id}")
    public Double getRevenueFromMovie(@PathVariable Long id) {
        return cinemaStatisticsService.getRevenueFromMovie(id);
    }

    @GetMapping("/movie_avg_rates/{id}")
    public Double getMovieRatesAvg(@PathVariable Long id) {
        return cinemaStatisticsService.getMovieRatesAvg(id);
    }

    @GetMapping("/movie_total_tickets/{id}")
    public Long getSumOfTickets(@PathVariable Integer id) {
        Map<Movie, Integer> map = cinemaStatisticsService.getSumOfTicketsPerMovie();
        for (Map.Entry<Movie, Integer> entry : map.entrySet()) {
            if (entry.getKey().getId().equals(id)) {
                return entry.getValue().longValue();
            }
        }
        return 0L;
    }

    @GetMapping("/user_most_money")
    public String getUserWhoSpentMostMoney() {
        CinemaUser user = cinemaStatisticsService.getUsersWithHighestSpendings().get(0);
        return "User: " + user.getEmail() + " spent the most money, he did " + cinemaStatisticsService.getReservationCountByUser(user.getId()) + " reservations overall";
    }
    
    @GetMapping("/most_popular_room")
    public String getMostPopularRoom() {
        return "Room: " + cinemaStatisticsService.getMostPopularRoom().getNumber() + " is the most popular room";
    }

    @GetMapping("/most_rated_movie")
    public String getMostRatedMovie() {
        Movie movie = cinemaStatisticsService.getMostRatedMovie();
        if (movie == null) {
            return "No rated movies";
        }
        return "Movie: " + movie.getTitle() + " is the most rated movie";
    }

    @GetMapping("/best_rated_movie")
    public String getBestRatedMovie() {
        Movie movie = cinemaStatisticsService.getBestRatedMovie();
        if (movie == null) {
            return "No rated movies";
        }
        return "Movie: " + movie.getTitle() + " is the best rated movie";
    }

    @GetMapping("/worst_rated_movie")
    public String getWorstRatedMovie() {
        Movie movie = cinemaStatisticsService.getWorstRatedMovie();
        if (movie == null) {
            return "No rated movies";
        }
        return "Movie: " + movie.getTitle() + " is the worst rated movie";
    }

    @GetMapping("/average_tickets_last_month")
    public String getAverageAmountOfTicketsRecently() {
        LocalDateTime now = LocalDateTime.now();
        return String.format("Average number of tickets this month: %.2f", cinemaStatisticsService.getAverageAmountOfTicketsRecently(now.minusMonths(1), now.minusDays(1)));
    }
}
