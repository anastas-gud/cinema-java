package ru.gudoshnikova.mainproject.controller;

import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.gudoshnikova.mainproject.dto.DatePeriodDTO;
import ru.gudoshnikova.mainproject.model.*;
import ru.gudoshnikova.mainproject.service.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final FilmService filmService;
    private final FilmSessionService filmSessionService;
    private final HallService hallService;
    private final FilmSessionsSeatsService filmSessionsSeatsService;
    private final CinemaService cinemaService;
    private final OrderService orderService;
    private final UserService userService;
    private final String UPLOAD_DIR = "src/main/resources/static/posters/";

    @Autowired
    public AdminController(FilmService filmService, FilmSessionService filmSessionService,
                           HallService hallService, FilmSessionsSeatsService filmSessionsSeatsService,
                           CinemaService cinemaService, OrderService orderService, UserService userService) {
        this.filmService = filmService;
        this.filmSessionService = filmSessionService;
        this.hallService = hallService;
        this.filmSessionsSeatsService = filmSessionsSeatsService;
        this.cinemaService = cinemaService;
        this.orderService = orderService;
        this.userService = userService;
    }
    @GetMapping("/films")
    public String films(Model model){
        model.addAttribute("films", filmService.getAllByDeletedFalse());
        return "admin/films";
    }
    @GetMapping("/films/get/{id}")
    public String get(@ModelAttribute("filmSession")FilmSession filmSession, @PathVariable("id") int id, Model model){
        model.addAttribute("film", filmService.getFilmById(id));
        model.addAttribute("filmSessions", filmSessionService.findByFilm(filmService.getFilmById(id)));
        model.addAttribute("filmSessionsByDate", filmSessionService.findByFilmAndDate(filmService.getFilmById(id)));
        model.addAttribute("halls", hallService.getAllAvailableHalls());
        return "admin/film";
    }
    @PostMapping("/film/addFilmSession")
    public String addFilmSession(@ModelAttribute("filmSession") FilmSession filmSession, @RequestParam("film") Integer filmId, Model model){
        filmSession.setFilm(filmService.getFilmById(filmId));
        FilmSession fs=filmSessionService.createFilmSession(filmSession);
        if(fs != null){
            filmSessionsSeatsService.addFilmSessionsSeatsByFilmSession(filmSession);
        }
        return "redirect:/admin/films/get/" + filmId;
    }
    @GetMapping("/film/edit/{id}")
    public String editFilm(@PathVariable("id") int id, Model model){
        model.addAttribute("film", filmService.getFilmById(id));
        return "admin/filmEdit";
    }
    @PatchMapping("/film/edit")
    public String editFilm(@ModelAttribute("film") Film film, Model model, @RequestParam("file") MultipartFile file){
        if(!file.isEmpty()){
            String fileName = film.getPosterFile();
            try {
                //todo filmService.updateFilm(film);
                Path path = Paths.get(UPLOAD_DIR + fileName);
                System.out.println(path.toAbsolutePath());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        filmService.updateFilm(film);
        return "redirect:/admin/films/get/" + film.getId();
    }
    @DeleteMapping("/films/delete/{id}")
    public String delete(@PathVariable("id") int id){
        filmService.deletedFilm(id);
        return "redirect:/admin/films";
    }
    @GetMapping("/films/add")
    public String addFilm(Model model){
        model.addAttribute("film", new Film());
        return "admin/filmAdd";
    }
    @PostMapping("/film/add")
    public String addFilm(@ModelAttribute("film") Film film, Model model, @RequestParam("file") MultipartFile file){
        String fileName=film.getTitle().replace(":", "_").toLowerCase()+".jpg";
        try {
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        film.setPosterFile(fileName);
        filmService.createFilm(film);
        return "redirect:/admin/films";
    }




    @GetMapping("/cinema")
    public String getAllCinema(Model model){
        model.addAttribute("cinema",cinemaService.getAllByIsDeletedFalse());
        return "admin/cinemaAll";
    }
    @GetMapping("/cinema/{id}")
    public String getCinema(@PathVariable("id") int id, Model model){
        model.addAttribute("cinema",cinemaService.getCinemaById(id));
        model.addAttribute("halls", hallService.getHallsByIsDeletedAndCinema(cinemaService.getCinemaById(id)));
        model.addAttribute("hall", new Hall());
        model.addAttribute("flag", false);
        return "admin/cinema";
    }
    @PostMapping("/cinema/addHall")
    public String addHall(@ModelAttribute("hall") Hall hall, @RequestParam("cinema") Integer cinemaId, Model model){
        //hallService.addHall(hall);
        model.addAttribute("cinema",cinemaService.getCinemaById(cinemaId));
        model.addAttribute("halls", hallService.getHallsByIsDeletedAndCinema(cinemaService.getCinemaById(cinemaId)));
        model.addAttribute("hall", hall);
        model.addAttribute("flag", true);
        model.addAttribute("seats", hallService.addSeatsInHall(hall).stream().toList());
        return "admin/cinema";
    }
    @PostMapping("/cinema/addHall2")
    public String addHall2(@ModelAttribute("hall") Hall hall,
                           @RequestParam(value = "states" , required = false) String[] states,
                           @RequestParam("cinemaId") Integer cinemaId, Model model){
        List<String> list= Arrays.asList(states);
        hallService.addHall(hall, list);
        return "redirect:/admin/cinema/"+cinemaId;
    }
    @DeleteMapping("/cinema/deleteHall/{id}")
    public String deleteHall(@PathVariable("id") int id){
        hallService.deletedHall(id);
        return "redirect:/admin/cinema/"+hallService.getHallById(id).getCinema().getId();
    }
    @GetMapping("/cinema/edit/{id}")
    public String editCinema(@PathVariable("id") int id, Model model){
        model.addAttribute("cinema", cinemaService.getCinemaById(id));
        return "admin/cinemaEdit";
    }
    @PatchMapping("/cinema/edit")
    public String editCinema(@ModelAttribute("cinema") Cinema cinema, Model model){
        cinemaService.updateCinema(cinema);
        return "redirect:/admin/cinema";
    }
    @GetMapping("/cinema/add")
    public String addCinema(Model model){
        model.addAttribute("cinema", new Cinema());
        return "admin/cinemaAdd";
    }
    @PostMapping("/cinema/add")
    public String addCinema(@ModelAttribute("cinema") Cinema cinema){
        cinemaService.createCinema(cinema);
        return "redirect:/admin/cinema";
    }
    @DeleteMapping("/cinema/delete/{id}")
    public String deleteCinema(@PathVariable("id") int id){
        cinemaService.deletedCinema(id);
        return "redirect:/admin/cinema";
    }



    @GetMapping("/orders")
    public String orders(Model model){
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders";
    }
    @GetMapping("/order/{id}")
    public String order(@PathVariable("id") int id, Model model){
        model.addAttribute("order", orderService.getOrderById(id));
        double totalCost=0;
        for(FilmSessionsSeats fss:orderService.getOrderById(id).getSeats()){
            totalCost+=fss.getFilmSession().getPrice();
        }
        model.addAttribute("totalCost", totalCost);
        return "admin/order";
    }
    @GetMapping("/order/user/{id}")
    public String user(@PathVariable("id") int id, Model model){
        model.addAttribute("user", userService.findById(id));
        return "admin/user";
    }
    @GetMapping("/users")
    public String users(Model model){
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }


    @GetMapping("/report")
    public String report(){
        return "admin/report";
    }
    @PostMapping("/report")
    public String report(@ModelAttribute("datePeriod") DatePeriodDTO datePeriodDTO, Model model) {
        model.addAttribute("totalCost", orderService.getTotalSum(datePeriodDTO));
        model.addAttribute("totalTickets", orderService.getTotalTickets(datePeriodDTO));
        return "admin/report";
    }
}
