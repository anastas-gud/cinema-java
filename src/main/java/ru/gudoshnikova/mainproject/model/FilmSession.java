package ru.gudoshnikova.mainproject.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;


@Entity
@Table(name = "film_sessions")
public class FilmSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "price")
    private double price;

    @ManyToOne()
    @JoinColumn(name = "film_id", foreignKey = @ForeignKey(name = "fk_id_film"), nullable = false)
    private Film film;

    @ManyToOne()
    @JoinColumn(name = "hall_id", foreignKey = @ForeignKey(name = "film_sessions_hall_id_fkey"), nullable = false)
    private Hall hall;

    @OneToMany(mappedBy = "filmSession")
    private Set<Order> orders;

    @OneToMany(mappedBy = "filmSession")
    private Set<FilmSessionsSeats> filmSessionsSeats;

    public FilmSession(){}

    public FilmSession(int id, LocalDate startDate, LocalTime startTime, double price, Film film, Hall hall, Set<Order> orders, Set<FilmSessionsSeats> filmSessionsSeats) {
        this.id = id;
        this.startDate = startDate;
        this.startTime = startTime;
        this.price = price;
        this.film = film;
        this.hall = hall;
        this.orders = orders;
        this.filmSessionsSeats = filmSessionsSeats;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Set<FilmSessionsSeats> getFilmSessionsSeats() {
        return filmSessionsSeats;
    }

    public void setFilmSessionsSeats(Set<FilmSessionsSeats> filmSessionsSeats) {
        this.filmSessionsSeats = filmSessionsSeats;
    }

    @Override
    public String toString() {
        return "FilmSession{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", startTime=" + startTime +
                ", price=" + price +
                '}';
    }
}
