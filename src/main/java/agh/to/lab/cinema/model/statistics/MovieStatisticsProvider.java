package agh.to.lab.cinema.model.statistics;

import agh.to.lab.cinema.model.movies.Movie;
import agh.to.lab.cinema.model.types.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieStatisticsProvider extends JpaRepository<Movie, Long> {

    @Query("select coalesce(sum(p.numberOfTickets * s.price), 0) from Purchase p join p.seance s where p.seance.movie.id = :movieId")
    Double getRevenueFromMovie(@Param("movieId") Long movieId);

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

    @Query("select avg(mr.rate) from MovieRate mr where mr.movie.id = :movieId")
    Double getMovieRatesAvg(@Param("movieId") Long movieId);

    @Query("select mr.movie, avg(mr.rate) from MovieRate mr group by mr.movie")
    List<Object[]> getMovieRatesAvgPerMovie();

    @Query("select mr.movie from MovieRate mr group by mr.movie order by count(mr.movie) desc limit 1")
    Movie getMostRatedMovie();

    @Query(value = "select mr.movie, count(mr.movie) from MovieRate mr group by mr.movie order by count(mr.movie) desc limit 5")
    List<Object[]> getTop5MostRatedMovies();

    @Query("select mr.movie from MovieRate mr group by mr.movie order by avg(mr.rate) desc limit 1")
    Movie getBestRatedMovie();

    @Query("select mr.movie from MovieRate mr group by mr.movie order by avg(mr.rate) asc limit 1")
    Movie getWorstRatedMovie();

    @Query("select mr.movie, avg(mr.rate) from MovieRate mr group by mr.movie order by avg(mr.rate) desc limit 5")
    List<Object[]> getTop5BestRatedMovies();

    @Query("select mr.movie, avg(mr.rate) from MovieRate mr group by mr.movie order by avg(mr.rate) asc limit 5")
    List<Object[]> getTop5WorstRatedMovies();

    @Query("select t from Type t join t.movies m join m.ratings mr group by t order by avg(mr.rate) desc limit 1")
    Type getBestRatedMovieType();

    @Query("select m from Movie m join m.types t join m.ratings mr where t = :type group by m order by avg(mr.rate) desc limit 1")
    Movie getBestMovieByType(@Param("type") Type type);

    @Query("select m, avg(mr.rate) from Movie m join m.types t join m.ratings mr where t = :type group by m order by avg(mr.rate) desc limit 5")
    List<Object[]> getTop5BestMoviesByType(@Param("type") Type type);

    @Query("select t, m from Type t join t.movies m join m.ratings mr group by t, m order by avg(mr.rate) desc limit 1")
    List<Object[]> getBestMoviePerType();
}
