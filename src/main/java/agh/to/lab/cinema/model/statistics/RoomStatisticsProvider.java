package agh.to.lab.cinema.model.statistics;

import agh.to.lab.cinema.model.rooms.Room;
import agh.to.lab.cinema.model.seances.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
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

    @Query("select r from Room r join r.seances s join s.purchases p group by r order by count(p.numberOfTickets) desc limit 1")
    Room getMostPopularRoom();

    @Query("select r, size(r.seances) from Room r order by size(r.seances) desc limit 5")
    List<Object[]> getTop5RoomsWithMostSeances();

    @Query("select r from Room r order by size(r.seances) desc limit 1")
    Room getRoomWithMostSeances();

    @Query("select s from Seance s where s.room = :room and s.startDate >= :start")
    List<Seance> getFutureSeancesInRoom(@Param("room") Room room, @Param("startDate") LocalDateTime start);
}
