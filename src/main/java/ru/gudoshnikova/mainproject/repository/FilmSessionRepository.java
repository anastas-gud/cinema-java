package ru.gudoshnikova.mainproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.gudoshnikova.mainproject.model.Cinema;
import ru.gudoshnikova.mainproject.model.Film;
import ru.gudoshnikova.mainproject.model.FilmSession;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface FilmSessionRepository extends JpaRepository<FilmSession, Integer> {
    @Query(nativeQuery = true,
            value = """
                    select distinct fs.*
                    from film_sessions fs
                    join films f on f.id = fs.film_id
                                        and fs.film_id=:filmId
                                        and (fs.start_date > :date or (fs.start_date= :date and fs.start_time >= :startTime))
            """)
    List<FilmSession> findByFilmAndDate(int filmId, LocalDate date, LocalTime startTime);
    List<FilmSession> findByFilmOrderByStartDateAscStartTimeAsc(Film film);
}
