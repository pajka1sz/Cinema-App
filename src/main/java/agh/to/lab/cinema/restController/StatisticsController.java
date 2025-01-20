package agh.to.lab.cinema.restController;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.statistics.CinemaStatisticsService;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
