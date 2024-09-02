package ru.gudoshnikova.mainproject.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Entity
@Table(name = "halls")
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "rows_count")
    private int rowsCount;

    @Column(name = "places_count_in_row")
    private int placesCount;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ManyToOne()
    @JoinColumn(name = "cinema_id", foreignKey = @ForeignKey(name = "fk_id_cinema"), nullable = false)
    private Cinema cinema;

    @OneToMany(mappedBy = "hall")
    private Set<Seat> seats;

    @OneToMany(mappedBy = "hall")
    private Set<FilmSession> filmSessions;
    public Hall(){}

    public Hall(int id, String name, int rowsCount, int placesCount, Cinema cinema, Set<Seat> seats, Set<FilmSession> filmSessions, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.rowsCount = rowsCount;
        this.placesCount = placesCount;
        this.cinema = cinema;
        this.seats = seats;
        this.filmSessions = filmSessions;
        this.isDeleted=isDeleted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRowsCount() {
        return rowsCount;
    }

    public void setRowsCount(int rowsCount) {
        this.rowsCount = rowsCount;
    }

    public int getPlacesCount() {
        return placesCount;
    }

    public void setPlacesCount(int placesCount) {
        this.placesCount = placesCount;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public Set<Seat> getSeats() {
        return seats;
    }

    public void setSeats(Set<Seat> seats) {
        this.seats = seats;
    }

    public Set<FilmSession> getFilmSessions() {
        return filmSessions;
    }

    public void setFilmSessions(Set<FilmSession> filmSessions) {
        this.filmSessions = filmSessions;
    }

    @Override
    public String toString() {
        return "Hall{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rowsCount=" + rowsCount +
                ", placesCount=" + placesCount +
                '}';
    }
}
