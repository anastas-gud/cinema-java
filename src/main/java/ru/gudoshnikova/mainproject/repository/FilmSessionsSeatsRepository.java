package ru.gudoshnikova.mainproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gudoshnikova.mainproject.model.FilmSession;
import ru.gudoshnikova.mainproject.model.FilmSessionsSeats;
import ru.gudoshnikova.mainproject.model.Seat;

import java.util.List;

@Repository
public interface FilmSessionsSeatsRepository extends JpaRepository<FilmSessionsSeats, Integer> {
    List<FilmSessionsSeats> findByFilmSession(FilmSession filmSession);
    FilmSessionsSeats findBySeatAndFilmSession(Seat seat, FilmSession filmSession);
}
