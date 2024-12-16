package agh.to.lab.cinema.model.movies;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void addMovie(Movie movie) {
        movieRepository.save(movie);
    }

    public void deleteMovie(Integer id) {
        movieRepository.deleteById(id);
    }

    public Movie getMovie(Integer id) {
        return movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));
    }

    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }
}
