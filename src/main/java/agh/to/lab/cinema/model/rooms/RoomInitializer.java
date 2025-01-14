package agh.to.lab.cinema.model.rooms;

import agh.to.lab.cinema.restController.RoomController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RoomInitializer implements CommandLineRunner {

    private static final int ROOM_NUMBER = 10;

    private final RoomController roomController;
    private final RoomRepository roomRepository;

    public RoomInitializer(RoomRepository roomRepository, RoomController roomController) {
        this.roomController = roomController;
        this.roomRepository = roomRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roomRepository.count() != 0) return;
        for (int i = 1; i <= ROOM_NUMBER; i++) {
            int capacity = new Random().nextInt(16) * 10 + 50;
            RoomDTO roomDTO = new RoomDTO(i, capacity);
            roomController.addRoom(roomDTO);
        }
    }
}
