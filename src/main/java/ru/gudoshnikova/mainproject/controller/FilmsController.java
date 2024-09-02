package ru.gudoshnikova.mainproject.controller;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gudoshnikova.mainproject.model.Film;
import ru.gudoshnikova.mainproject.service.CinemaService;
import ru.gudoshnikova.mainproject.service.FilmService;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/films")
public class FilmsController {
    private final FilmService filmService;
    private final CinemaService cinemaService;
    @Autowired
    public FilmsController(FilmService filmService, CinemaService cinemaService) {
        this.filmService = filmService;
        this.cinemaService = cinemaService;
    }
    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute("films", filmService.getAllByDeletedFalse());
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("tomorrow", LocalDate.now().plusDays(1));
        model.addAttribute("minDate", LocalDate.now().plusDays(2));
        return "films/films";
    }
    @PostMapping()
    public String index(@ModelAttribute("startDate") LocalDate filmSessionStartDate, Model model) {
        model.addAttribute("films", filmService.getAllByDate(filmSessionStartDate));
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("tomorrow", LocalDate.now().plusDays(1));
        model.addAttribute("minDate", LocalDate.now().plusDays(2));
        return "films/films";
    }
    @PostMapping("/get/{id}")
    public String getFilm(@RequestParam("startDate") LocalDate startDate, @PathVariable int id, Model model){
        model.addAttribute("film", filmService.getFilmById(id));
        model.addAttribute("cinema", cinemaService.getAvailableCinema(id, startDate));
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("now", LocalTime.now());

        if(startDate==null){
            startDate=LocalDate.now();
        }
        model.addAttribute("startDate", startDate);
        return "films/film";
    }
}
