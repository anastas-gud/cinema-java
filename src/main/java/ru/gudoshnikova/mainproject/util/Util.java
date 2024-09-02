package ru.gudoshnikova.mainproject.util;

import ru.gudoshnikova.mainproject.model.Cinema;
import ru.gudoshnikova.mainproject.model.*;
import ru.gudoshnikova.mainproject.repository.FilmSessionRepository;
import ru.gudoshnikova.mainproject.repository.SeatRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Util {
    public static FilmSessionsSeats getSeatByRowAndSeat(List<FilmSessionsSeats> list, Integer row, Integer seat, Hall hall) {
        return list.stream().filter(s ->
                s.getSeat().getRow() == row && s.getSeat().getSeat() == seat
                        && s.getSeat().getHall().equals(hall)).findFirst().orElse(null);
    }
    public static Seat getHallSeatByRowAndSeat(List<Seat> list, Integer row, Integer seat, Hall hall) {
        return list.stream().filter(s ->
                s.getRow() == row && s.getSeat() == seat
                        && s.getHall().equals(hall)).findFirst().orElse(null);
    }
    public static List<FilmSession> getFilmSessionsByCinemaOrderByTime(Cinema cinema) {
        List<Hall> halls = new ArrayList<>(cinema.getHalls());
        List<FilmSession> filmSessions = new ArrayList<>();
        for (Hall hall : halls) {
            filmSessions.addAll(hall.getFilmSessions());
        }
        filmSessions.sort((FilmSession a1, FilmSession a2) ->
                a1.getStartTime().isAfter(a2.getStartTime()) ? 1 : -1);
        return filmSessions;
    }
    public static List<Order> getFutureOrders(Set<Order> orders) {
        return orders.stream().filter(o->o.getFilmSession().getStartDate().isAfter(LocalDate.now())
                || (o.getFilmSession().getStartDate().equals(LocalDate.now())
                && o.getFilmSession().getStartTime().isAfter(LocalTime.now())))
                .sorted(Comparator.comparing(Order::getDate).reversed())
                .sorted(Comparator.comparing(Order::getTime).reversed()).toList();
    }
    public static List<Order> getPastOrders(Set<Order> orders) {
        return orders.stream().filter(o->o.getFilmSession().getStartDate().isBefore(LocalDate.now())
                || (o.getFilmSession().getStartDate().equals(LocalDate.now())
                && o.getFilmSession().getStartTime().isBefore(LocalTime.now())))
                .sorted(Comparator.comparing(Order::getDate).reversed())
                .sorted(Comparator.comparing(Order::getTime).reversed()).toList();
    }
    public static Integer min(Integer a, Integer b) {
        return a < b ? a : b;
    }
    public static Double getTotalCost(Order order) {
        double totalCost=0;
        for(FilmSessionsSeats fss:order.getSeats()){
            totalCost+=fss.getFilmSession().getPrice();
        }
        return totalCost;
    }
    public static int getCountExistPlaces(Hall hall){
        return (int) hall.getSeats().stream().filter(Seat::isExist).count();
    }
}
