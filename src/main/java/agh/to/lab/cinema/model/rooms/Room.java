package agh.to.lab.cinema.model.rooms;

import agh.to.lab.cinema.model.seances.Seance;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
public class Room {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @Column(nullable = false, unique = true)
    private Integer number;

    @Setter
    @Getter
    @Column(nullable = false)
    private Integer capacity;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seance> seances;

    public Room() {}

    public Room(Integer number, Integer capacity) {
        this.number = number;
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return number.toString();
    }
}
