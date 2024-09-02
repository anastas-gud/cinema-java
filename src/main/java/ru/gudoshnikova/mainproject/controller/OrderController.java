package ru.gudoshnikova.mainproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gudoshnikova.mainproject.model.*;
import ru.gudoshnikova.mainproject.service.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final SeatService seatService;
    private final FilmSessionService filmSessionService;
    private final UserService userService;
    private final CardService cardService;
    private final OrderService orderService;
    private final FilmSessionsSeatsService filmSessionsSeatsService;
    private final EmailService emailService;
    @Autowired
    public OrderController(SeatService seatService, FilmSessionService filmSessionService,
                           UserService userService, OrderService orderService, CardService cardService,
                           FilmSessionsSeatsService filmSessionsSeatsService, EmailService emailService) {
        this.seatService = seatService;
        this.filmSessionService = filmSessionService;
        this.userService = userService;
        this.orderService = orderService;
        this.filmSessionsSeatsService = filmSessionsSeatsService;
        this.emailService = emailService;
        this.cardService=cardService;
    }
    @GetMapping("/selectSeat")
    public String getSeats(@RequestParam(value = "filmSessionId") int filmSessionId, Model model) {
        FilmSession filmSession = filmSessionService.getFilmSession(filmSessionId);
        model.addAttribute("filmSession", filmSession);
        model.addAttribute("filmSessionSeats", filmSessionsSeatsService.findByFilmSession(filmSession));
        model.addAttribute("seats", filmSessionsSeatsService.findByFilmSession(filmSession).stream().map(FilmSessionsSeats::getSeat).collect(Collectors.toList()));
        return "orders/selectSeat";
    }
    @PostMapping("/process")
    public String process(@ModelAttribute("order") Order order,
                          @RequestParam("filmSessionId") Integer filmSessionId,
                          @RequestParam(value = "states" , required = false) Integer[] states,
                          Model model, @RequestParam("points") Integer points){

        if (states==null){
            model.addAttribute("error", "Вы не выбрали ни одно место.");
            FilmSession filmSession = filmSessionService.getFilmSession(filmSessionId);
            model.addAttribute("filmSession", filmSession);
            model.addAttribute("filmSessionSeats", filmSessionsSeatsService.findByFilmSession(filmSession));
            model.addAttribute("seats", filmSessionsSeatsService.findByFilmSession(filmSession).stream().map(FilmSessionsSeats::getSeat).collect(Collectors.toList()));
            return "orders/selectSeat";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        User user= userService.findByUsername(userDetail.getUsername());
        FilmSession filmSession = filmSessionService.getFilmSession(filmSessionId);

        List<Integer> list= Arrays.asList(states);
        List<FilmSessionsSeats> seatList=list.stream().map(filmSessionsSeatsService::getFilmSessionsSeatsById).collect(Collectors.toList());

        order.setUser(user);
        order.setFilmSession(filmSession);
        order.setSeats(new HashSet<>(seatList));
        model.addAttribute("totalCost", filmSession.getPrice()*seatList.size());
        model.addAttribute("seats", seatList);
        model.addAttribute("filmSession", filmSession);
        model.addAttribute("points", points);
        model.addAttribute("states", states);
        if(points!=null){
            model.addAttribute("total", filmSession.getPrice()*seatList.size()-points);
        }
        return "orders/processOne";
    }
    @PostMapping("/processTwo")
    public String processTwo(@ModelAttribute("order") Order order, Model model, @RequestParam(value = "points" , required = false) Integer points, @RequestParam(value = "states" , required = false) Integer[] states){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        User user= userService.findByUsername(userDetail.getUsername());

        model.addAttribute("points", points);
        model.addAttribute("states", states);
        if(points!=null){
            double totalCost=order.getFilmSession().getPrice()*order.getSeats().size()-points;
            model.addAttribute("totalCost", totalCost);
            model.addAttribute("pointForBalance", ((double)user.getCard().getCashback()/100)*totalCost);
        }
        else{
            double totalCost=order.getFilmSession().getPrice()*order.getSeats().size();
            model.addAttribute("totalCost", totalCost);
            model.addAttribute("pointForBalance", ((double)user.getCard().getCashback()/100)*totalCost);
        }
        return "orders/processTwo";
    }
    @PostMapping("/decoration")
    public String decorationOrder(@ModelAttribute("order") Order order, @RequestParam("totalCost") Double totalCost, @RequestParam("pointForBalance") Double pointForBalance) throws InterruptedException, ExecutionException, TimeoutException {
        order.setDate(LocalDate.now());
        order.setTime(LocalTime.now());

        Order dto=orderService.saveOrder(order, order.getFilmSession().getPrice()-totalCost, pointForBalance);

        if(dto==null){
            return "orders/error";
        }
        emailService.sendEmail(order);
        return "orders/success";


        //        Thread.sleep(7000);
//        synchronized(this) {
//            List<Order> orders=orderService.getAllOrders();
//            for(Order order1:orders){
//                if(order1.getDate().equals(order.getDate())){
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
//                    String v1=order1.getTime().format(formatter);
//                    String v2=order.getTime().format(formatter);
//                    if(v1.equals(v2)){
//                        if(order1.getUser().equals(order.getUser())){
//                            if(order1.getFilmSession().equals(order.getFilmSession())){
//                                if(order1.getSeats().equals(order.getSeats())){
//                                    emailService.sendEmail(order);
//                                    return "orders/success";
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return "orders/error";
    }
}
//todo    каждому месту bool есть ли оно       при создании, вводим макс кол-во мест, рядов и выбираем
//todo    те, которых нет