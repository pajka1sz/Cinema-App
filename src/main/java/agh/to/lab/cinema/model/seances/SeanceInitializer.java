package agh.to.lab.cinema.model.seances;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.movies.MovieService;
import agh.to.lab.cinema.model.rooms.Room;
import agh.to.lab.cinema.model.rooms.RoomService;
import agh.to.lab.cinema.restController.SeanceController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
public class SeanceInitializer implements CommandLineRunner {
    private final SeanceController seanceController;
    private final MovieService movieService;
    private final RoomService roomService;
    private final SeanceRepository seanceRepository;

    public SeanceInitializer(SeanceRepository seanceRepository, SeanceController seanceController, MovieService movieService, RoomService roomService) {
        this.seanceController = seanceController;
        this.movieService = movieService;
        this.roomService = roomService;
        this.seanceRepository = seanceRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (seanceRepository.count() != 0) return;
        List<Movie> movies = movieService.getMovies();
        List<Room> rooms = roomService.getRooms();
        Random random = new Random();

        if (movies.isEmpty() || rooms.isEmpty()) {
            System.out.println("No movies or rooms available to create seances.");
            return;
        }

        for (int i = 0; i < 100; i++) {
            Movie movie = movies.get(random.nextInt(movies.size()));
            Room room = rooms.get(random.nextInt(rooms.size()));

            LocalDateTime startDate = LocalDateTime.now()
                    .plusDays(random.nextInt(30))
                    .withHour(7 + random.nextInt(17))
                    .withMinute(random.nextInt(4) * 15)
                    .withSecond(0)
                    .withNano(0);

            float price = 10 + random.nextFloat() * (50 - 10);
            price = Math.round(price * 100) / 100.0f;

            SeanceDTO seanceDTO = new SeanceDTO(
                    Math.toIntExact(movie.getId()),
                    Math.toIntExact(room.getId()),
                    startDate,
                    price
            );

            seanceController.addSeance(seanceDTO);
        }

        System.out.println("100 seances initialized.");
    }
}
