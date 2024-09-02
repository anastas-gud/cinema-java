package ru.gudoshnikova.mainproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.gudoshnikova.mainproject.model.Cinema;
import ru.gudoshnikova.mainproject.model.Film;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Integer> {
    @Query(nativeQuery = true,
            value = """
                    select distinct c.*
                    from cinema c
                    join halls h on c.id = h.cinema_id
                    join film_sessions fs on h.id = fs.hall_id
                                        and fs.film_id=:filmId
                                        and fs.start_date = :date
                                        and fs.start_time >= :startTime
                                        and c.is_deleted = false
                                        and h.is_deleted = false
            """)
    List<Cinema> getAllAvailable(@Param(value = "startTime") LocalTime startTime, @Param(value = "filmId") Integer filmId, @Param("date") LocalDate date);
    List<Cinema> getAllByIsDeleted(boolean isDeleted);
}
