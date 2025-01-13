package agh.to.lab.cinema.model.statistics;

import agh.to.lab.cinema.model.purchases.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PurchasesStatisticsProvider extends JpaRepository<Purchase, Long> {
    // statisticsProvider.findLargePurchases(BigDecimal.valueOf(10))
    @Query("select p from Purchase p where p.numberOfTickets * p.seance.price > :amount")
    List<Purchase> findLargePurchases(@Param("amount") BigDecimal amount);

    // LocalDateTime now = LocalDateTime.now();
    // statisticsProvider.getAverageAmountOfTicketsRecently(now, now.minusMonths(1))
    @Query("select avg(p.numberOfTickets) from Seance s join s.purchases p where s.startDate <= :end and s.startDate >= :start")
    Double getAverageAmountOfTicketsRecently(@Param("startDate") LocalDateTime start, @Param("endDate") LocalDateTime end);
}
