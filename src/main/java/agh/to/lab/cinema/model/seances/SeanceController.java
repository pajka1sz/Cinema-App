package agh.to.lab.cinema.model.seances;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/seance")
public class SeanceController {

    private final SeanceService seanceService;

    public SeanceController(SeanceService seanceService) {
        this.seanceService = seanceService;
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
        Seance seance = new Seance(
                seanceDTO.getMovie(),
                seanceDTO.getRoom(),
                seanceDTO.getStartDate(),
                seanceDTO.getPrice()
        );
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
