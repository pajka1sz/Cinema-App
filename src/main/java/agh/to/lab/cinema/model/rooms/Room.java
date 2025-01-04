package agh.to.lab.cinema.model.rooms;

import agh.to.lab.cinema.model.seances.Seance;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer number;

    @Column(nullable = false)
    private Integer capacity;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seance> seances;

    public Room() {}

    public Room(Integer number, Integer capacity) {
        this.number = number;
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public Integer getNumber() {
        return number;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {this.capacity = capacity;}

    public void setNumber(Integer number) {this.number = number;}
}
