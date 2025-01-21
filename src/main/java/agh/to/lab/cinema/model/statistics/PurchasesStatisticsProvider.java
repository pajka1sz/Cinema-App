package agh.to.lab.cinema.model.statistics;

import agh.to.lab.cinema.model.purchases.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchasesStatisticsProvider extends JpaRepository<Purchase, Long> {
    // statisticsProvider.findLargePurchases(BigDecimal.valueOf(10))
    @Query("select p from Purchase p where p.numberOfTickets * p.seance.price > :amount")
    List<Purchase> findLargePurchases(@Param("amount") BigDecimal amount);

    // LocalDateTime now = LocalDateTime.now();
    // statisticsProvider.getAverageAmountOfTicketsRecently(now, now.minusMonths(1))
    @Query("select coalesce(avg(p.numberOfTickets), 0) from Seance s join s.purchases p where s.startDate <= :end and s.startDate >= :start")
    Double getAverageAmountOfTicketsRecently(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
