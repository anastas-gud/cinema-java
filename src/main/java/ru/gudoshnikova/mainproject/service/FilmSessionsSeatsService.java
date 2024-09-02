package ru.gudoshnikova.mainproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gudoshnikova.mainproject.model.FilmSession;
import ru.gudoshnikova.mainproject.model.FilmSessionsSeats;
import ru.gudoshnikova.mainproject.model.Hall;
import ru.gudoshnikova.mainproject.model.Seat;
import ru.gudoshnikova.mainproject.repository.FilmSessionsSeatsRepository;

import java.util.List;

@Service
@Transactional
public class FilmSessionsSeatsService {
    private final FilmSessionsSeatsRepository filmSessionsSeatsRepository;
    @Autowired
    public FilmSessionsSeatsService(FilmSessionsSeatsRepository filmSessionsSeatsRepository) {
        this.filmSessionsSeatsRepository = filmSessionsSeatsRepository;
    }
    public List<FilmSessionsSeats> getAllFilmSessionsSeats() {
        return filmSessionsSeatsRepository.findAll();
    }
    public FilmSessionsSeats getFilmSessionsSeatsById(int id) {
        return filmSessionsSeatsRepository.findById(id).orElse(null);
    }
    public void addFilmSessionsSeats(FilmSessionsSeats filmSessionsSeats) {
        filmSessionsSeatsRepository.save(filmSessionsSeats);
    }
    public void updateFilmSessionsSeats(FilmSessionsSeats filmSessionsSeats) {
        filmSessionsSeatsRepository.save(filmSessionsSeats);
    }
    public void deleteFilmSessionsSeats(int id) {
        filmSessionsSeatsRepository.deleteById(id);
    }
    public List<FilmSessionsSeats> findByFilmSession(FilmSession filmSession) {
        return filmSessionsSeatsRepository.findByFilmSession(filmSession);
    }
    public FilmSessionsSeats findBySeat(Seat seat, FilmSession filmSession) {
        return filmSessionsSeatsRepository.findBySeatAndFilmSession(seat, filmSession);
    }
    public void addFilmSessionsSeatsByFilmSession(FilmSession filmSession) {
        for(Seat seat:filmSession.getHall().getSeats()) {
            FilmSessionsSeats filmSessionsSeats = new FilmSessionsSeats();
            filmSessionsSeats.setFilmSession(filmSession);
            filmSessionsSeats.setSeat(seat);
            filmSessionsSeatsRepository.save(filmSessionsSeats);
        }
    }
}
