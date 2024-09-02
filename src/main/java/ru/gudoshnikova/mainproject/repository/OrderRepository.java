package ru.gudoshnikova.mainproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.gudoshnikova.mainproject.model.Order;

import java.time.LocalDate;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query(nativeQuery = true,
            value = "select sum(fs.price) from film_sessions as fs " +
                    "join film_sessions_seats as fss on fs.id = fss.film_session_id " +
                    "join orders_seats as os on fss.id = os.film_session_seat_id " +
                    "join orders as o on o.id = os.order_id " +
                    "where o.date between :startDate and :endDate")
    Double getTotalCost(@Param(value = "startDate") LocalDate startDate, @Param(value = "endDate") LocalDate endDate);
    @Query(nativeQuery = true,
            value = """
                select count(*)
                    from orders o
                    join orders_seats os on o.id = os.order_id
                                        and o.date between :startDate and :endDate
            """)
    Integer getTotalTickets(@Param(value = "startDate") LocalDate startDate, @Param(value = "endDate") LocalDate endDate);
}
