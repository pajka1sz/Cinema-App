package agh.to.lab.cinema.model.rooms;

public class RoomDTO {
    private Integer number;
    private Integer capacity;

    public RoomDTO(Integer number, Integer capacity) {
        this.number = number;
        this.capacity = capacity;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
