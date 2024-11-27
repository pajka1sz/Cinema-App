package agh.to.lab.cinema.model.rooms;

import jakarta.persistence.*;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer number;

    @Column(nullable = false)
    private Integer capacity;

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

}
