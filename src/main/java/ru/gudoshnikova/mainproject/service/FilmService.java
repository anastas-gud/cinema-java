package ru.gudoshnikova.mainproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gudoshnikova.mainproject.model.Film;
import ru.gudoshnikova.mainproject.repository.FilmRepository;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FilmService {
    private final FilmRepository filmRepository;
    @Autowired
    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }
    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }
    public Page<Film> getAll(PageRequest pageable) {
        return filmRepository.findAll(pageable);
    }
    public List<Film> getAllAvailable(LocalDate date){
        return filmRepository.getAllAvailable(LocalTime.now(), date);
    }
    public List<Film> getAllByDate(LocalDate filmSessionStartDate) {
        List<Film> films = new ArrayList<>();
        if (!filmSessionStartDate.isBefore(LocalDate.now())) {
            films = LocalDate.now().equals(filmSessionStartDate)
                    ? filmRepository.getAllByDate(filmSessionStartDate, LocalTime.now())
                    : filmRepository.getAllByDate(filmSessionStartDate, LocalTime.of(0, 0));
        }
        return films;
    }
    public List<Film> getAllByDeletedFalse(){
        return filmRepository.getAllByIsDeletedAndRental(false);
    }
    public List<Film> getAllByDeletedTrue(){
        return filmRepository.getAllByIsDeletedAndRental(true);
    }
    public Film getFilmById(int id) {
        return filmRepository.findById(id).orElse(null);
    }
    public Film createFilm(Film film) {
        return filmRepository.save(film);
    }
    public Film updateFilm(Film film) {
        return filmRepository.save(film);
    }
    public void deleteFilm(int id) {
        filmRepository.deleteById(id);
    }
    public void deletedFilm(int id) {
        getFilmById(id).setDeleted(true);
        updateFilm(getFilmById(id));
    }
}
