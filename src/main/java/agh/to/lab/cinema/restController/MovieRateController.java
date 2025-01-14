package agh.to.lab.cinema.restController;

import agh.to.lab.cinema.model.movies.MovieService;
import agh.to.lab.cinema.model.rates.MovieRate;
import agh.to.lab.cinema.model.rates.MovieRateDTO;
import agh.to.lab.cinema.model.rates.MovieRateService;
import agh.to.lab.cinema.model.users.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/movie_rate")
public class MovieRateController {
    private MovieRateService movieRateService;
    private MovieService movieService;
    private UserService userService;

    public MovieRateController(MovieRateService movieRateService, MovieService movieService, UserService userService) {
        this.movieRateService = movieRateService;
        this.movieService = movieService;
        this.userService = userService;
    }

    @PostMapping(value = "/add_test")
    public ResponseEntity<String> addTestMovieRates() {
        try {
            MovieRate movieRate = new MovieRate(
                    movieService.getMovie(1),
                    userService.getUser(1),
                    5,
                    "Great movie! I am going to see it few more times!"
            );
            movieRateService.addMovieRate(movieRate);
            MovieRate movieRate2 = new MovieRate(
                    movieService.getMovie(2),
                    userService.getUser(1),
                    3,
                    "Not the best film i've ever watched."
            );
            movieRateService.addMovieRate(movieRate2);
            return new ResponseEntity<>("Test movie rate added.", HttpStatus.OK);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Movie or uer not found for adding test movie rates.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping()
    public List<MovieRate> getMovieRates() {
        return movieRateService.getMovieRates();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<MovieRate> getMovieRate(@PathVariable Integer id) {
        try {
            MovieRate rate = movieRateService.getMovieRate(id);
            return new ResponseEntity<>(rate, HttpStatus.OK);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> addMovieRate(@RequestBody MovieRateDTO movieRateDTO) {
        List<MovieRate> allMovieRates = movieRateService.getMovieRates();
        for (MovieRate movieRate: allMovieRates) {
            if (movieRate.getMovie().getId().equals(movieRateDTO.getMovie_id())
                    && movieRate.getUser().getId().equals(movieRateDTO.getUser_id().longValue()))
                return new ResponseEntity<>("This user has already given a rate for this film!", HttpStatus.BAD_REQUEST);
        }

        MovieRate rate = new MovieRate(
                movieService.getMovie(movieRateDTO.getMovie_id()),
                userService.getUser(movieRateDTO.getUser_id()),
                movieRateDTO.getRate(),
                movieRateDTO.getDescription()
        );
        movieRateService.addMovieRate(rate);
        return new ResponseEntity<>("Movie rate added.", HttpStatus.OK);
    }

    @PutMapping(value = "/update_rate/{id}")
    public ResponseEntity<String> updateMovieRateRate(@PathVariable Integer id, @RequestBody Integer newRate) {
        try {
            MovieRate movieRate = movieRateService.getMovieRate(id);
            movieRate.setRate(newRate);
            movieRateService.addMovieRate(movieRate);
            return new ResponseEntity<>("Movie rate's rate updated.", HttpStatus.OK);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Movie rate not found for updating rate.", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update_description/{id}")
    public ResponseEntity<String> updateMovieRateDescription(@PathVariable Integer id, @RequestBody String newDescription) {
        try {
            MovieRate movieRate = movieRateService.getMovieRate(id);
            movieRate.setDescription(newDescription);
            movieRateService.addMovieRate(movieRate);
            return new ResponseEntity<>("Movie rate's description updated.", HttpStatus.OK);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Movie rate not found for updating description.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteMovieRate(@PathVariable Integer id) {
        try {
            movieRateService.getMovieRate(id);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Movie rate not found for deletion.", HttpStatus.NOT_FOUND);
        }
        movieRateService.deleteMovieRate(id);
        return new ResponseEntity<>("Movie rate deleted.", HttpStatus.OK);
    }
}
