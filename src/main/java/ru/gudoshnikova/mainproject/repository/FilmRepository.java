package ru.gudoshnikova.mainproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.gudoshnikova.mainproject.model.Film;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
// and current_date between f.start_rental and f.end_rental
@Repository
public interface FilmRepository extends JpaRepository<Film, Integer> {
    @Query(nativeQuery = true,
            value = """
                    select distinct f.*
                    from films f
                    join film_sessions fs on f.id = fs.film_id
                                        and fs.start_date = :startDate
                                        and fs.start_time >= :startTime
                                        and f.is_deleted = false
                                        and current_date between f.start_rental and f.end_rental
            """)
    List<Film> getAllAvailable(@Param(value = "startTime") LocalTime startTime, @Param(value = "startDate") LocalDate startDate);
    @Query(nativeQuery = true,
            value = """
                    select distinct f.*
                    from films f
                    join film_sessions fs on f.id = fs.film_id
                                        and fs.start_date = :startDate
                                        and fs.start_time >= :startTime
                                        and f.is_deleted = false
                                        and (:startDate between f.start_rental and f.end_rental)
            """)
    List<Film> getAllByDate(@Param(value = "startDate") LocalDate startDate, @Param(value = "startTime") LocalTime startTime);
    @Query(nativeQuery = true,
            value = """
                    select distinct f.*
                    from films f
                        where f.is_deleted = :isDeleted
                        and current_date between f.start_rental and f.end_rental
            """)
    List<Film> getAllByIsDeletedAndRental(boolean isDeleted);     //TODO start_rental end_rental
}
