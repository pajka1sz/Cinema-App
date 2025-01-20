package agh.to.lab.cinema.model.statistics;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.purchases.Purchase;
import agh.to.lab.cinema.model.types.Type;
import agh.to.lab.cinema.model.users.CinemaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CinemaUserStatisticsProvider extends JpaRepository<CinemaUser, Long> {
    @Query("select CinemaUser from CinemaUser cu join cu.purchases p group by cu.id order by count(p) desc limit 1")
    CinemaUser getUserWithMostReservation();

    @Query("select count(*) from Purchase p where p.id = :userId")
    Long getReservationCountByUser(@Param("userId") Long userId);

    @Query("select cu from CinemaUser cu join cu.purchases p group by cu.id order by sum(p.numberOfTickets * p.seance.price) desc")
    List<CinemaUser> getUsersWithHighestSpendings();

    @Query(value = "select CinemaUser from CinemaUser cu join MovieRate mr group by cu.id order by count(mr.user) desc  limit 1")
    CinemaUser getUserWithMostRates();
}
