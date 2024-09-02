package ru.gudoshnikova.mainproject.model;

import jakarta.annotation.Priority;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Primary;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hall_seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "row")
    private int row;

    @Column(name = "seat")
    private int seat;

    @Column(name = "is_exist")
    private boolean isExist=true;

    @ManyToOne
    @JoinColumn(name = "hall_id", foreignKey = @ForeignKey(name = "hall_seats_hall_id_fkey"), nullable = false)
    private Hall hall;

    @OneToMany(mappedBy = "seat")
    private Set<FilmSessionsSeats> filmSessionsSeats;
    public Seat() {}

    public Seat(int id, int row, int seat, Hall hall, Set<FilmSessionsSeats> filmSessionsSeats, boolean isExist) {
        this.id = id;
        this.row = row;
        this.seat = seat;
        this.hall = hall;
        this.filmSessionsSeats = filmSessionsSeats;
        this.isExist=isExist;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public Set<FilmSessionsSeats> getFilmSessionsSeats() {
        return filmSessionsSeats;
    }

    public void setFilmSessionsSeats(Set<FilmSessionsSeats> filmSessionsSeats) {
        this.filmSessionsSeats = filmSessionsSeats;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", row=" + row +
                '}';
    }
}
