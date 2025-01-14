package agh.to.lab.cinema.model.rates;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieRateService {
    private MovieRateRepository movieRateRepository;

    public MovieRateService(MovieRateRepository movieRateRepository) {
        this.movieRateRepository = movieRateRepository;
    }

    public MovieRateService() {

    }

    public List<MovieRate> getMovieRates() {
        return movieRateRepository.findAll();
    }

    public MovieRate getMovieRate(Integer id) {
        return movieRateRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie rate not found."));
    }

    public void addMovieRate(MovieRate movieRate) {
        movieRateRepository.save(movieRate);
    }

    public void deleteMovieRate(Integer id) {
        movieRateRepository.deleteById(id);
    }
}
