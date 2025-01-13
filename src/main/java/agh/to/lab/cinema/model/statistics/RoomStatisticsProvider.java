package agh.to.lab.cinema.model.statistics;

import agh.to.lab.cinema.model.rooms.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomStatisticsProvider extends JpaRepository<Room, Long> {


    //     List<Object[]> results = statisticsProvider.getAverageOccupancyPerRoom();
    //
    //    return results.stream()
    //                  .collect(Collectors.toMap(
    //                      row -> (Room) row[0],
    //                      row -> (Double) row[1]
    //                  ));
    @Query("select s.room, avg(p.numberOfTickets) from Purchase p join p.seance s group by s.room")
    List<Object[]> getAverageOccupancyPerRoom();
}
