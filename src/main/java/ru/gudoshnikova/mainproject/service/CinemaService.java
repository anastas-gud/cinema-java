package ru.gudoshnikova.mainproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gudoshnikova.mainproject.model.Cinema;
import ru.gudoshnikova.mainproject.repository.CinemaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class CinemaService {
    private final CinemaRepository cinemaRepository;
    @Autowired
    public CinemaService(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }
    public List<Cinema> getAllCinema() {
        return cinemaRepository.findAll();
    }
    public List<Cinema> getAvailableCinema(Integer filmId, LocalDate date) {
        if (date.equals(LocalDate.now())) {
            return cinemaRepository.getAllAvailable(LocalTime.now(), filmId, date);
        }
        return cinemaRepository.getAllAvailable(LocalTime.of(0, 0), filmId, date);
    }
    public Cinema getCinemaById(int id) {
        return cinemaRepository.findById(id).orElse(null);
    }
    public List<Cinema> getAllByIsDeletedFalse() {
        return cinemaRepository.getAllByIsDeleted(false);
    }
    public Cinema createCinema(Cinema cinema) {
        return cinemaRepository.save(cinema);
    }
    public Cinema updateCinema(Cinema cinema) {
        return cinemaRepository.save(cinema);
    }
    public void deleteCinema(int id) {
        cinemaRepository.deleteById(id);
    }
    public void deletedCinema(int id) {
        getCinemaById(id).setDeleted(true);
        updateCinema(getCinemaById(id));
    }
}
