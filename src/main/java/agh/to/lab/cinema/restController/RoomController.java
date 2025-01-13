package agh.to.lab.cinema.restController;

import agh.to.lab.cinema.model.rooms.Room;
import agh.to.lab.cinema.model.rooms.RoomDTO;
import agh.to.lab.cinema.model.rooms.RoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/room")
public class RoomController {

    private final RoomService roomService;
    private static final String baseUrl = "http://localhost:8080/room";
    public static String getBaseUrl() {
        return baseUrl;
    }

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<Room> getRooms() {
        return roomService.getRooms();
    }

    @GetMapping("/{id}")
    public Room getRoom(@PathVariable Integer id) {
        return roomService.getRoom(id);
    }

    @PostMapping(value = "/add", consumes = "application/json")
    public String addRoom(@RequestBody RoomDTO roomDTO) {
        Room room = new Room(
                roomDTO.getNumber(),
                roomDTO.getCapacity()
        );
        roomService.addRoom(room);
        return "Room added";
    }

    @PutMapping("/update_capacity/{id}")
    public String updateRoomCapacity(@PathVariable Integer id, @RequestBody Integer capacity) {
        Room roomToUpdate = roomService.getRoom(id);
        roomToUpdate.setCapacity(capacity);
        roomService.addRoom(roomToUpdate);
        return "Room updated";
    }

    @PutMapping("/update_number/{id}")
    public String updateRoomNumber(@PathVariable Integer id, @RequestBody Integer number) {
        Room roomToUpdate = roomService.getRoom(id);
        roomToUpdate.setNumber(number);
        roomService.addRoom(roomToUpdate);
        return "Room updated";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Integer id) {
        roomService.deleteRoom(id);
        return "Room deleted";
    }
}