package agh.to.lab.cinema.restController;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.movies.MovieService;
import agh.to.lab.cinema.model.rooms.Room;
import agh.to.lab.cinema.model.rooms.RoomService;
import agh.to.lab.cinema.model.seances.Seance;
import agh.to.lab.cinema.model.seances.SeanceDTO;
import agh.to.lab.cinema.model.seances.SeanceService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/seance")
public class SeanceController {

    private final SeanceService seanceService;
    private final MovieService movieService;
    private final RoomService roomService;
    private static final String baseUrl = "http://localhost:8080/seance";
    public static String getBaseUrl() {
        return baseUrl;
    }

    public SeanceController(SeanceService seanceService, MovieService movieService, RoomService roomService) {
        this.seanceService = seanceService;
        this.movieService = movieService;
        this.roomService = roomService;
    }

    @GetMapping
    public List<Seance> getSeances() {
        return seanceService.getSeances();
    }

    @GetMapping("/{id}")
    public Seance getSeance(@PathVariable Integer id) {
        return seanceService.getSeance(id);
    }

    @PostMapping(value = "/add", consumes = "application/json")
    public String addSeance(@RequestBody SeanceDTO seanceDTO) {
        System.out.println("TUUUU");
        Movie movie = movieService.getMovie(seanceDTO.getMovie_id());
        Room room = roomService.getRoom(seanceDTO.getRoom_id());
        System.out.println(seanceDTO.toString());
        Seance seance = new Seance(movie, room, seanceDTO.getStartDate(), seanceDTO.getPrice());
        seanceService.addSeance(seance);
        return "Seance added";
    }

    @PutMapping("/update_price/{id}")
    public String updateSeancePrice(@PathVariable Integer id, @RequestBody float price) {
        Seance seanceToUpdate = seanceService.getSeance(id);
        seanceToUpdate.setPrice(price);
        seanceService.addSeance(seanceToUpdate);
        return "Seance updated";
    }

    @PutMapping("/update_start_date/{id}")
    public String updateSeanceStartDate(@PathVariable Integer id, @RequestBody LocalDateTime startDate) {
        Seance seanceToUpdate = seanceService.getSeance(id);
        seanceToUpdate.setStartDate(startDate);
        seanceService.addSeance(seanceToUpdate);
        return "Seance updated";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteSeance(@PathVariable Integer id) {
        seanceService.deleteSeance(id);
        return "Seance deleted";
    }
}
