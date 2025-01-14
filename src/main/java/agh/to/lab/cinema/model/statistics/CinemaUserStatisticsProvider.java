package agh.to.lab.cinema.model.statistics;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.purchases.Purchase;
import agh.to.lab.cinema.model.types.Type;
import agh.to.lab.cinema.model.users.CinemaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CinemaUserStatisticsProvider extends JpaRepository<CinemaUser, Long> {
    @Query(value = "select cu.* from PURCHASE p join CINEMA_USER cu on p.USER_ID = cu.ID group by p.USER_ID order by count(p.USER_ID) desc limit 1", nativeQuery = true)
    CinemaUser getUserWithMostReservation();

    @Query("select count(*) from Purchase p where p.id = :userId")
    Long getReservationCountByUser(@Param("userId") Long userId);

    @Query("select CinemaUser from CinemaUser cu join cu.purchases p group by cu.id order by sum(p.numberOfTickets * p.seance.price) desc limit 1")
    CinemaUser getUserWithHighestSpendings();
}
