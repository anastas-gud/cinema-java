package ru.gudoshnikova.mainproject.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "films")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "start_rental")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startRental;

    @Column(name = "end_rental")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endRental;

    @Column(name = "duration")
    private int duration;

    @Column(name = "genre")
    private String genre;

    @Column(name = "poster_file")
    private String posterFile;

    @Column(name = "country")
    private String country;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @OneToMany(mappedBy = "film")
    private Set<FilmSession> filmSessions;
    public Film(){}

    public Film(int id, String title, String description, LocalDate startRental, LocalDate endRental, int duration, String genre, String posterFile, String country, Set<FilmSession> filmSessions, boolean isDeleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startRental = startRental;
        this.endRental = endRental;
        this.duration = duration;
        this.genre = genre;
        this.posterFile = posterFile;
        this.country = country;
        this.filmSessions = filmSessions;
        this.isDeleted = isDeleted;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartRental() {
        return startRental;
    }

    public void setStartRental(LocalDate startRental) {
        this.startRental = startRental;
    }

    public LocalDate getEndRental() {
        return endRental;
    }

    public void setEndRental(LocalDate endRental) {
        this.endRental = endRental;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPosterFile() {
        return posterFile;
    }

    public void setPosterFile(String posterFile) {
        this.posterFile = posterFile;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Set<FilmSession> getFilmSessions() {
        return filmSessions;
    }

    public void setFilmSessions(Set<FilmSession> filmSessions) {
        this.filmSessions = filmSessions;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startRental=" + startRental +
                ", endRental=" + endRental +
                ", duration=" + duration +
                ", genre='" + genre + '\'' +
                ", posterFile='" + posterFile + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
