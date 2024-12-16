package agh.to.lab.cinema.model.seances;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeanceService {
    private final SeanceRepository seanceRepository;

    public SeanceService(SeanceRepository seanceRepository) {
        this.seanceRepository = seanceRepository;
    }

    public void addSeance(Seance seance) {
        seanceRepository.save(seance);
    }

    public void deleteSeance(Integer id) {
        seanceRepository.deleteById(id);
    }

    public Seance getSeance(Integer id) {
        return seanceRepository.findById(id).orElseThrow(() -> new RuntimeException("Seance not found"));
    }

    public List<Seance> getSeances() {
        return seanceRepository.findAll();
    }
}
