package agh.to.lab.cinema.model.movies;

import agh.to.lab.cinema.model.types.MovieType;
import agh.to.lab.cinema.model.types.Type;
import agh.to.lab.cinema.model.types.TypeService;
import agh.to.lab.cinema.model.users.CinemaUser;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/movie")
public class MovieController {

    private final MovieService movieService;
    private final TypeService typeService;

    public MovieController(MovieService movieService, TypeService typeService) {
        this.movieService = movieService;
        this.typeService = typeService;
    }

    @GetMapping
    public List<Movie> getMovies() {
        return movieService.getMovies();
    }

    @GetMapping("test")
    public String test() {
        List<Movie> list = movieService.getMovies();
        StringBuilder sb = new StringBuilder();
        for (Movie m : list) {
            sb.append(m.toString()).append(" ");
        }
        return sb.toString();
    }

    @PostMapping(value = "/add", consumes = "application/json")
    public String addMovie(@RequestBody MovieDTO movieDTO) {

        Movie movie = new Movie(
                movieDTO.getTitle(),
                movieDTO.getDescription(),
                movieDTO.getLength(),
                movieDTO.getThumbnail()
        );

        // Handle the type (you may need to fetch it from the database or map it to an entity)
        Set<String> t = movieDTO.getTypes();
        for (String s : t) {
            if (!MovieType.isTypeValid(s)) {
                return "Invalid type";
            }
        }
        Set<Type> types = t.stream().map(String::toUpperCase).map(typeService::findTypeByName).collect(Collectors.toSet());
        movie.setTypes(types);
        movieService.addMovie(movie);
        return "Movie added";
    }

    @PutMapping("/update_thumbnail/{id}")
    public String updateMovieThumbnail(@PathVariable Integer id, @RequestBody String thumbnail) {
        Movie movieToUpdate = movieService.getMovie(id);
        movieToUpdate.setThumbnail(thumbnail);
        movieService.addMovie(movieToUpdate);
        return "Movie updated";
    }

    @PutMapping("/update_description/{id}")
    public String updateMovieDescription(@PathVariable Integer id, @RequestBody String description) {
        Movie movieToUpdate = movieService.getMovie(id);
        movieToUpdate.setDescription(description);
        movieService.addMovie(movieToUpdate);
        return "Movie updated";
    }

    @PutMapping("/update_types/{id}")
    public String updateMovieTypes(@PathVariable Integer id, @RequestBody Set<String> t) {
        Movie movieToUpdate = movieService.getMovie(id);
        for (String s : t) {
            if (!MovieType.isTypeValid(s)) {
                return "Invalid type";
            }
        }
        Set<Type> types = t.stream().map(String::toUpperCase).map(typeService::findTypeByName).collect(Collectors.toSet());
        movieToUpdate.setTypes(types);
        movieService.addMovie(movieToUpdate);
        return "Movie updated";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        movieService.deleteMovie(id);
        return "Movie deleted";
    }
}
