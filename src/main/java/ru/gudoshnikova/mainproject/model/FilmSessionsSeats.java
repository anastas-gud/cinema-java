package ru.gudoshnikova.mainproject.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "film_sessions_seats")
public class FilmSessionsSeats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "is_booked")
    private boolean isBooked;

    @ManyToOne()
    @JoinColumn(name = "film_session_id", foreignKey = @ForeignKey(name = "film_sessions_seats_film_session_id_fkey"), nullable = false)
    private FilmSession filmSession;

    @ManyToOne()
    @JoinColumn(name = "seat_id", foreignKey = @ForeignKey(name = "film_sessions_seats_seat_id_fkey"), nullable = false)
    private Seat seat;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE
            },
            mappedBy = "seats")
    private Set<Order> orders;
    public FilmSessionsSeats(){}

    public FilmSessionsSeats(int id, boolean isBooked, FilmSession filmSession, Seat seat, Set<Order> orders) {
        this.id = id;
        this.isBooked = isBooked;
        this.filmSession = filmSession;
        this.seat = seat;
        this.orders = orders;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public FilmSession getFilmSession() {
        return filmSession;
    }

    public void setFilmSession(FilmSession filmSession) {
        this.filmSession = filmSession;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "FilmSessionsSeats{" +
                "id=" + id +
                ", isBooked=" + isBooked +
                '}';
    }
}
