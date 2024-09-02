package ru.gudoshnikova.mainproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gudoshnikova.mainproject.model.Cinema;
import ru.gudoshnikova.mainproject.model.Film;
import ru.gudoshnikova.mainproject.model.FilmSession;
import ru.gudoshnikova.mainproject.repository.FilmSessionRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class FilmSessionService {
    private final FilmSessionRepository filmSessionRepository;
    @Autowired
    public FilmSessionService(FilmSessionRepository filmSessionRepository) {
        this.filmSessionRepository = filmSessionRepository;
    }
    public List<FilmSession> getFilmSessions() {
        return filmSessionRepository.findAll();
    }
    public FilmSession getFilmSession(int id) {
        return filmSessionRepository.findById(id).orElse(null);
    }
    public FilmSession createFilmSession(FilmSession filmSession) {
        for(FilmSession filmSession1:getFilmSessions()) {
            if(filmSession1.getStartDate().equals(filmSession.getStartDate()) &&
                filmSession1.getStartTime().equals(filmSession.getStartTime()) &&
                filmSession1.getHall().equals(filmSession.getHall()) &&
                filmSession1.getFilm().equals(filmSession.getFilm())){
                return null;
            }
            if(filmSession1.getHall().equals(filmSession.getHall()) &&
                filmSession1.getStartDate().equals(filmSession.getStartDate()) &&
                filmSession.getStartTime().isBefore(filmSession1.getStartTime().plusMinutes(180)) &&
                filmSession1.getFilm().equals(filmSession.getFilm())){
                return null;
            }
        }
        return filmSessionRepository.save(filmSession);
    }
    public FilmSession updateFilmSession(FilmSession filmSession) {
        return filmSessionRepository.save(filmSession);
    }
    public void deleteFilmSession(int id) {
        filmSessionRepository.deleteById(id);
    }
    public List<FilmSession> findByFilm(Film film) {
        return filmSessionRepository.findByFilmOrderByStartDateAscStartTimeAsc(film);
    }
    public List<FilmSession> findByFilmAndDate(Film film) {
        return filmSessionRepository.findByFilmAndDate(film.getId(), LocalDate.now(), LocalTime.now());
    }
}
