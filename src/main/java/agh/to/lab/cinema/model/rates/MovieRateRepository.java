package agh.to.lab.cinema.model.rates;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRateRepository extends JpaRepository<MovieRate, Integer> {
}
