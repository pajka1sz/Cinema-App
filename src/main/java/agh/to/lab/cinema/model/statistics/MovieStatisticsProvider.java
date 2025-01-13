package agh.to.lab.cinema.model.statistics;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.types.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieStatisticsProvider extends JpaRepository<Movie, Long> {

    @Query("select coalesce(sum(p.numberOfTickets * s.price), 0) from Purchase p join p.seance s where p.seance.movie = :movie")
    Double getRevenueFromMovie(@Param("movie") Movie movie);

    //     List<Object[]> results = statisticsProvider.getSumOfTicketsPerMovie();
    //
    //    return results.stream()
    //                  .collect(Collectors.toMap(
    //                      row -> (Movie) row[0],
    //                      row -> ((Number) row[1]).intValue()
    //                  ));
    @Query("select s.movie, sum(p.numberOfTickets) from Purchase p join p.seance s group by s.movie")
    List<Object[]> getSumOfTicketsPerMovie();


    @Query("select t from Type t join t.movies m join m.seances s join s.purchases p group by t order by count(p.numberOfTickets) desc limit 3")
    List<Type> getTop3MostPopularTypes();
}
