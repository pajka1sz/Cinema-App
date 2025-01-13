package agh.to.lab.cinema.model.movies;

import agh.to.lab.cinema.MovieFetcher;
import agh.to.lab.cinema.restController.MovieController;
import com.opencsv.CSVReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.FileReader;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class MovieInitializer implements CommandLineRunner {

    private static final int NUMBER_OF_MOVIES = 100;

    private final MovieController movieController;
    private final MovieRepository movieRepository;

    public MovieInitializer(MovieController movieController, MovieRepository movieRepository) {
        this.movieController = movieController;
        this.movieRepository = movieRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (movieRepository.count() != 0) return;
        
        String csvFilePath = ResourceUtils.getFile("classpath:movies.csv").getPath();
        try (CSVReader csvReader = new CSVReader(new FileReader(csvFilePath))) {
            List<String> movieTitles = csvReader.readAll()
                    .stream()
                    .skip(1)
                    .map(row -> row[0])
                    .collect(Collectors.toList());

            Collections.shuffle(movieTitles, new Random());
            List<String> randomMovies = movieTitles.stream()
                    .distinct()
                    .limit(NUMBER_OF_MOVIES)
                    .toList();

            MovieFetcher movieFetcher = new MovieFetcher();

            for (String title : randomMovies) {
                try {
                    MovieDTO movieDTO = movieFetcher.getMovieDetails(title);
                    movieController.addMovie(movieDTO);
                    System.out.println("Added movie: " + title);
                } catch (Exception e) {
                    System.err.println("Failed to add movie: " + title + " | Error: " + e.getMessage());
                }
            }
        }
    }
}
